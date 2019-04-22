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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.graduate.thesis.backend.security.SecurityConstants.USER_ROLE_ID;


/**
 * @author Huy Pham
 */

@RestController
@RequestMapping(Constant.AUTH_API)
public class AuthController extends AbstractBasedAPI{

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;


    @PostMapping(Constant.LOGIN_API)
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {


        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getPhone(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.createToken(authentication);

        return responseUtil.successResponse(token);
    }

    @PostMapping(Constant.REGISTRY_API)
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserAccount signUpRequest) {

        if(userAccountRepository.existsByPhone(signUpRequest.getPhone())) {
            throw new BadRequestException("Phone already in use.");
        }

        // Creating user's account
        UserAccount user = new UserAccount();
        user.setPhone(signUpRequest.getPhone());
        user.setPassword(signUpRequest.getPassword());
        user.setProvider(AuthProvider.local);
        user.setRoleId(USER_ROLE_ID);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        UserAccount result = userAccountRepository.save(user);

        return responseUtil.successResponse(result);
    }



}
