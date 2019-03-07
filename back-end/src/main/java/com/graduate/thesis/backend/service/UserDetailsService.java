package com.graduate.thesis.backend.service;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Huy Pham
 */

public interface UserDetailsService {

    public UserDetails loadUserByUsername(String username);

}
