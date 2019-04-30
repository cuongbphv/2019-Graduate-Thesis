package com.graduate.thesis.backend.model.response;

import com.graduate.thesis.backend.entity.Permission;
import com.graduate.thesis.backend.entity.Role;
import com.graduate.thesis.backend.entity.UserAccount;
import com.graduate.thesis.backend.entity.UserProfile;
import com.graduate.thesis.backend.entity.model.UserSetting;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

/**
 * @author Huy Pham
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrentUserResponse {

    private String userId;

    private String phone;

    private String email;

    private String firstName;

    private String lastName;

    private Role role;

    private String avatarUrl;

    private List<Permission> permission;

    private Collection<? extends GrantedAuthority> authorities;

    private UserSetting setting;


    public CurrentUserResponse(UserAccount userAccount, UserProfile userProfile,
                               Role role, List<Permission> permission, Collection<? extends GrantedAuthority> authorities){

        this.userId = userAccount.getId();
        this.phone = userAccount.getPhone();
        this.email = userAccount.getEmail();
        this.avatarUrl = userProfile.getAvatarUrl();
        this.firstName = userProfile.getFirstName();
        this.lastName = userProfile.getLastName();
        this.role = role;
        this.permission = permission;
        this.authorities = authorities;
        this.setting = userProfile.getSetting();
    }

}
