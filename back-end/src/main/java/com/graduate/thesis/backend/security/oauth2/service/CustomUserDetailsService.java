package com.graduate.thesis.backend.security.oauth2.service;

import com.graduate.thesis.backend.entity.UserAccount;
import com.graduate.thesis.backend.exception.ResourceNotFoundException;
import com.graduate.thesis.backend.repository.UserAccountRepository;
import com.graduate.thesis.backend.security.oauth2.user.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * @author Huy Pham
 */

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserAccountRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        UserAccount user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email : " + email)
                );

        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(String id) {
        UserAccount user = userRepository.findByUserId(id);

        if(user == null){
            throw new ResourceNotFoundException("User", "id", id);
        }

        return UserPrincipal.create(user);
    }
}
