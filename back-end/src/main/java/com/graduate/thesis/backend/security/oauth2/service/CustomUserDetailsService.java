package com.graduate.thesis.backend.security.oauth2.service;

import com.graduate.thesis.backend.entity.Permission;
import com.graduate.thesis.backend.entity.Role;
import com.graduate.thesis.backend.entity.UserAccount;
import com.graduate.thesis.backend.exception.ApplicationException;
import com.graduate.thesis.backend.exception.ResourceNotFoundException;
import com.graduate.thesis.backend.repository.PermissionRepository;
import com.graduate.thesis.backend.repository.RoleRepository;
import com.graduate.thesis.backend.repository.UserAccountRepository;
import com.graduate.thesis.backend.security.oauth2.user.UserPrincipal;
import com.graduate.thesis.backend.util.APIStatus;
import com.graduate.thesis.backend.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Huy Pham
 */

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserAccountRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String phone)
            throws UsernameNotFoundException {
        UserAccount user = userRepository.findByPhone(phone)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with phone : " + phone)
                );

//        return UserPrincipal.create(user);
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
    }

    @Transactional
    public UserDetails loadUserById(String id) {
        UserAccount user = userRepository.findByUserId(id);

        if(user == null){
            throw new ResourceNotFoundException("User", "id", id);
        }

//        return UserPrincipal.create(user);
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
    }
}
