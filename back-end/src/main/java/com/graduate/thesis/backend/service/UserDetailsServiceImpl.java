package com.graduate.thesis.backend.service;

import com.graduate.thesis.backend.entity.User;
import com.graduate.thesis.backend.repository.SessionUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptyList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private SessionUserRepository sessionUserRepository;

    public UserDetailsServiceImpl(SessionUserRepository sessionUserRepository) {
        this.sessionUserRepository = sessionUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = sessionUserRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), emptyList());
    }
}
