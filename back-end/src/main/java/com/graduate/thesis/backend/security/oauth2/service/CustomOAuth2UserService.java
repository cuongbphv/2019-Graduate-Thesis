package com.graduate.thesis.backend.security.oauth2.service;

import com.graduate.thesis.backend.entity.Permission;
import com.graduate.thesis.backend.entity.Role;
import com.graduate.thesis.backend.entity.UserAccount;
import com.graduate.thesis.backend.entity.UserProfile;
import com.graduate.thesis.backend.exception.OAuth2AuthenticationProcessingException;
import com.graduate.thesis.backend.repository.PermissionRepository;
import com.graduate.thesis.backend.repository.RoleRepository;
import com.graduate.thesis.backend.security.AuthProvider;
import com.graduate.thesis.backend.repository.UserAccountRepository;
import com.graduate.thesis.backend.security.oauth2.user.UserPrincipal;
import com.graduate.thesis.backend.security.oauth2.user.OAuth2UserInfo;
import com.graduate.thesis.backend.security.oauth2.user.OAuth2UserInfoFactory;
import com.graduate.thesis.backend.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.graduate.thesis.backend.security.SecurityConstants.USER_ROLE_ID;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserAccountRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    UserProfileService userProfileService;

    @Autowired
    private PermissionRepository permissionRepository;

    public CustomOAuth2UserService(){

        //For fix fb response json
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        enhanceJsonMessageConverter(restTemplate);

        super.setRestOperations(restTemplate);
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        Optional<UserAccount> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        UserAccount user;
        if(userOptional.isPresent()) {
            user = userOptional.get();
            if(!user.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        user.getProvider() + " account. Please use your " + user.getProvider() +
                        " account to login.");
            }
            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();

        Role role = roleRepository.findByRoleId(user.getRoleId());

        List<String> permissionIds = new ArrayList<>();

        if(role.getPermissions() != null) {
            permissionIds.addAll(role.getPermissions());
        }

        if(user.getPersonalPermissions() != null) {
            permissionIds.addAll(user.getPersonalPermissions());
        }

        List<Permission> permissions = permissionRepository.findByListId(permissionIds);

        for (Permission permission : permissions) {
            authorities.add(new SimpleGrantedAuthority(permission.getName()));
        }

        return UserPrincipal.create(user, authorities);

//        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private UserAccount registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {

        UserAccount user = new UserAccount();

        user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        user.setProviderId(oAuth2UserInfo.getId());
        // user.setName(oAuth2UserInfo.getName());
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setRoleId(USER_ROLE_ID);
        user.setStatus(1);
        // user.setImageUrl(oAuth2UserInfo.getImageUrl());

        UserAccount createdUser = userRepository.save(user);

        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(createdUser.getId());
        userProfile.setFirstName(oAuth2UserInfo.getName());
        userProfile.setCreatedDate(new Date());
        userProfile.setModifiedDate(new Date());
        userProfile.setRatingCount(20);
        userProfile.setRatingValue(60);
        userProfile.setRatingAverage(3);
        userProfile.setStatus(1);
        userProfileService.save(userProfile);

        return createdUser;
    }

    private UserAccount updateExistingUser(UserAccount existingUser, OAuth2UserInfo oAuth2UserInfo) {
//        existingUser.(oAuth2UserInfo.getName());
//        existingUser.setImageUrl(oAuth2UserInfo.getImageUrl());
        return userRepository.save(existingUser);
    }


    private void enhanceJsonMessageConverter(RestTemplate restTemplate) {

        HttpMessageConverter<?> jsonMessageConverter = restTemplate.getMessageConverters().stream()
                .filter(c -> c instanceof MappingJackson2HttpMessageConverter)
                .findFirst()
                .orElse(null);

        if (jsonMessageConverter == null) {
            return;
        }

        List<MediaType> supportedMediaTypes = new ArrayList<>(jsonMessageConverter.getSupportedMediaTypes());
        supportedMediaTypes.add(MediaType.valueOf("text/javascript"));
        ((AbstractHttpMessageConverter) jsonMessageConverter).setSupportedMediaTypes(supportedMediaTypes);

    }

}
