package com.graduate.thesis.backend.controller;

import com.graduate.thesis.backend.entity.UserAccount;
import com.graduate.thesis.backend.entity.UserProfile;
import com.graduate.thesis.backend.exception.ApplicationException;
import com.graduate.thesis.backend.exception.BadRequestException;
import com.graduate.thesis.backend.model.request.LoginRequest;
import com.graduate.thesis.backend.model.response.RestAPIResponse;
import com.graduate.thesis.backend.repository.UserAccountRepository;
import com.graduate.thesis.backend.security.AuthProvider;
import com.graduate.thesis.backend.security.TokenProvider;
import com.graduate.thesis.backend.security.otp.Sms4Api;
import com.graduate.thesis.backend.service.UserProfileService;
import com.graduate.thesis.backend.util.APIStatus;
import com.graduate.thesis.backend.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Date;
import java.util.Random;

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
    private UserProfileService userProfileService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;


    @PostMapping(Constant.LOGIN_API)
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        try {

            UserAccount user = userAccountRepository.findByPhone(loginRequest.getPhone())
                    .orElseThrow(() ->
                            new ApplicationException(APIStatus.ERR_USER_NOT_FOUND)
                    );

            if (user.getStatus() == Constant.Status.PENDING.getValue()) {
                throw new ApplicationException(APIStatus.ERR_USER_NOT_CONFIRM_OTP);
            }

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
        catch (BadCredentialsException ex){
            throw new ApplicationException(APIStatus.ERR_USER_INCORRECT_PASSWORD);
        }
    }

    @PostMapping(Constant.REGISTRY_API)
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserAccount signUpRequest) {

        if(userAccountRepository.existsByPhone(signUpRequest.getPhone())) {
            throw new ApplicationException(APIStatus.ERR_PHONE_IN_USE);
        }

        String otp = generateOTP(5);

        // Creating user's account
        UserAccount user = new UserAccount();
        user.setPhone(signUpRequest.getPhone());
        user.setProvider(AuthProvider.local);
        user.setRoleId(USER_ROLE_ID);
        user.setStatus(Constant.Status.PENDING.getValue());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setOtp(otp);

        UserAccount result = userAccountRepository.save(user);

        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(user.getId());
        userProfile.setFirstName(user.getPhone());
        userProfile.setCreatedDate(new Date());
        userProfile.setModifiedDate(new Date());
        userProfile.setStatus(Constant.Status.PENDING.getValue());

        userProfileService.save(userProfile);

        // send message
        Sms4Api.getInstance().sendSmsOTP(signUpRequest.getPhone(), otp);

        return responseUtil.successResponse(result);
    }

    @GetMapping(Constant.SUBMIT_OTP_API)
    public ResponseEntity<RestAPIResponse> sendOTP(
            @RequestParam("phone") String phone
    ) {

        UserAccount user = userAccountRepository.findByPhone(phone)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with phone : " + phone)
                );

        if (user.getStatus() == Constant.Status.ACTIVE.getValue()) {
            throw new ApplicationException(APIStatus.ERR_USER_ACTIVE_NOW);
        }

        String otp = generateOTP(5);
        user.setOtp(otp);
        userAccountRepository.save(user);

        Sms4Api.getInstance().sendSmsOTP(phone, otp);

        return responseUtil.successResponse("Sent");
    }

    @PostMapping(Constant.SUBMIT_OTP_API)
    public ResponseEntity<RestAPIResponse> submitOTP(
            @RequestBody LoginRequest loginRequest
    ) {

        UserAccount user = userAccountRepository.findByPhone(loginRequest.getPhone())
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with phone : " + loginRequest.getPhone())
                );

        UserProfile userProfile = userProfileService.findByUserId(user.getId())
                .orElseThrow(() -> new ApplicationException(APIStatus.ERR_USER_PROFILE_NOT_FOUND));

        if (user.getOtp().equals(loginRequest.getOtp())) {

            user.setStatus(Constant.Status.ACTIVE.getValue());
            userProfile.setStatus(Constant.Status.ACTIVE.getValue());
            userAccountRepository.save(user);
            userProfileService.save(userProfile);

            return responseUtil.successResponse("OK");
        }

        throw new ApplicationException(APIStatus.ERR_WRONG_OTP_CODE);
    }

    private static String generateOTP(int length) {
        String numbers = "1234567890";
        Random random = new Random();
        char[] otp = new char[length];

        for(int i = 0; i< length ; i++) {
            otp[i] = numbers.charAt(random.nextInt(numbers.length()));
        }
        return new String(otp);
    }

}
