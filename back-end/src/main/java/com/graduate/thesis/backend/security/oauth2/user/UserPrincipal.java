package com.graduate.thesis.backend.security.oauth2.user;

import com.graduate.thesis.backend.entity.UserAccount;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class UserPrincipal implements OAuth2User, OidcUser, UserDetails {

    private String id;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;
    private Map<String, Object> claims;
    private OidcUserInfo oidcUserInfo;
    private OidcIdToken oidcIdToken;

    public UserPrincipal(String id, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserPrincipal create(UserAccount user, List<GrantedAuthority> authorities) {

        return new UserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }

    public static UserPrincipal create(UserAccount user) {
        List<GrantedAuthority> authorities = Collections.
                singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        return new UserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }


    public static UserPrincipal create(UserAccount user, Map<String, Object> attributes) {
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;


//
//        this.authorities = new ArrayList<>();
//
//        Role role = roleService.findByRoleId("5c9bbbe605f5f129bd02cc83");
//
//        List<String> permissionIds = new ArrayList<>();
//        permissionIds.addAll(role.getPermissions());
//        permissionIds.addAll(userAccount.getPersonalPermissions());
//
//        List<Permission> permissions = permissionService.findByListId(permissionIds);
//
//        for (Permission permission : permissions) {
//            authorities.add(new SimpleGrantedAuthority(permission.getName()));
//        }
//
//        return authorities;

    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }

    @Override
    public Map<String, Object> getClaims() {
        return this.claims;
    }

    public void setClaims(Map<String, Object> claims){
        this.claims = claims;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return this.oidcUserInfo;
    }

    public void setOidcUserInfo(OidcUserInfo oidcUserInfo){
        this.oidcUserInfo = oidcUserInfo;
    }

    @Override
    public OidcIdToken getIdToken() {
        return this.oidcIdToken;
    }

    public void setOidcIdToken(OidcIdToken oidcIdToken){
        this.oidcIdToken = oidcIdToken;
    }
}
