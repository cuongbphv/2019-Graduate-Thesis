package com.graduate.thesis.backend.service;

import com.graduate.thesis.backend.entity.UserAccount;
import com.graduate.thesis.backend.repository.UserAccountRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptyList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserAccountRepository userAccountRepository;

    public UserDetailsServiceImpl(UserAccountRepository sessionUserRepository) {
        this.userAccountRepository = sessionUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount = userAccountRepository.findByUsername(username);
        if (userAccount == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(userAccount.getUsername(), userAccount.getPassword(), emptyList());
    }
}
