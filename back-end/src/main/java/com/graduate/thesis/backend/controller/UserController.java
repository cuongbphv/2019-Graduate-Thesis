package com.graduate.thesis.backend.controller;

import com.graduate.thesis.backend.Application;
import com.graduate.thesis.backend.entity.Permission;
import com.graduate.thesis.backend.entity.Role;
import com.graduate.thesis.backend.entity.UserAccount;
import com.graduate.thesis.backend.entity.UserProfile;
import com.graduate.thesis.backend.exception.ApplicationException;
import com.graduate.thesis.backend.model.request.ChangePasswordRequest;
import com.graduate.thesis.backend.model.response.CurrentUserResponse;
import com.graduate.thesis.backend.model.response.RestAPIResponse;
import com.graduate.thesis.backend.security.CurrentUser;
import com.graduate.thesis.backend.security.oauth2.user.UserPrincipal;
import com.graduate.thesis.backend.service.PermissionService;
import com.graduate.thesis.backend.service.RoleService;
import com.graduate.thesis.backend.service.UserAccountService;
import com.graduate.thesis.backend.service.UserProfileService;
import com.graduate.thesis.backend.util.APIStatus;
import com.graduate.thesis.backend.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(Constant.USER_API)
public class UserController extends AbstractBasedAPI{

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @RequestMapping(value = Constant.CURRENT_USER, method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {

        UserAccount userAccount = userAccountService.findActiveUserById(userPrincipal.getId());
        if(userAccount == null){
            throw new ApplicationException(APIStatus.ERR_USER_NOT_FOUND);
        }

        Optional<UserProfile> userProfile = userProfileService.findActiveByUserId(userPrincipal.getId());
        if(!userProfile.isPresent()){
            throw new ApplicationException(APIStatus.ERR_USER_PROFILE_NOT_FOUND);
        }

        Role role = roleService.findByRoleId(userAccount.getRoleId());
        if(role == null){
            throw new ApplicationException(APIStatus.ERR_USER_ROLE_NOT_FOUND);
        }

        List<Permission> permissionList  = new ArrayList<>();
        if(userAccount.getPersonalPermissions() != null && !userAccount.getPersonalPermissions().isEmpty()) {
            permissionList = permissionService.findByListId(userAccount.getPersonalPermissions());
        }

        CurrentUserResponse currentUserResponse = new CurrentUserResponse(userAccount, userProfile.get(),
                role , permissionList, userPrincipal.getAuthorities());

        return responseUtil.successResponse(currentUserResponse);
    }

    @RequestMapping(value = Constant.CHANGE_PASSWORD, method = RequestMethod.PUT)
    public ResponseEntity<RestAPIResponse> changeUserPassword(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody @Valid ChangePasswordRequest passwordRequest
            ) {

        UserAccount userAccount = userAccountService.findActiveUserById(userPrincipal.getId());
        if (userAccount == null) {
            throw new ApplicationException(APIStatus.ERR_USER_NOT_FOUND);
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userAccount.getPhone(),
                            passwordRequest.getCurrentPassword()
                    )
            );
        } catch (BadCredentialsException ex) {
            throw new ApplicationException(APIStatus.ERR_USER_INCORRECT_PASSWORD);
        }

        userAccount.setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));

        userAccountService.save(userAccount);

        return responseUtil.successResponse("Change Password Successfully");
    }
}
