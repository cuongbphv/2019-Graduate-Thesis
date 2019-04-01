package com.graduate.thesis.backend.controller;

import com.graduate.thesis.backend.entity.UserAccount;
import com.graduate.thesis.backend.exception.ResourceNotFoundException;
import com.graduate.thesis.backend.repository.UserAccountRepository;
import com.graduate.thesis.backend.security.CurrentUser;
import com.graduate.thesis.backend.security.oauth2.user.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @GetMapping("/user/me")
    public UserAccount getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userAccountRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }
}
