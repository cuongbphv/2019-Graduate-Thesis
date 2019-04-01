package com.graduate.thesis.backend.controller;

import com.graduate.thesis.backend.entity.UserAccount;
import com.graduate.thesis.backend.exception.BadRequestException;
import com.graduate.thesis.backend.model.request.LoginRequest;
import com.graduate.thesis.backend.model.response.ApiResponse;
import com.graduate.thesis.backend.model.response.AuthResponse;
import com.graduate.thesis.backend.repository.UserAccountRepository;
import com.graduate.thesis.backend.security.AuthProvider;
import com.graduate.thesis.backend.security.TokenProvider;
import com.graduate.thesis.backend.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;


/**
 * @author Huy Pham
 */

@RestController
@RequestMapping(Constant.AUTH_API)
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;

    @PostMapping(Constant.LOGIN_API)
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.createToken(authentication);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping(Constant.REGISTRY_API)
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserAccount signUpRequest) {

        if(userAccountRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("Email address already in use.");
        }

        // Creating user's account
        UserAccount user = new UserAccount();
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(signUpRequest.getPassword());
        user.setProvider(AuthProvider.local);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        UserAccount result = userAccountRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/me")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "User registered successfully@"));
    }


}
