package com.graduate.thesis.backend.controller;

import com.graduate.thesis.backend.entity.UserAccount;
import com.graduate.thesis.backend.repository.UserAccountRepository;
import com.graduate.thesis.backend.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Huy Pham
 */

@RestController
public class AuthController {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @RequestMapping(value = Constant.REGISTRY_API, method = RequestMethod.POST)
    public void signUp(@RequestBody UserAccount userAccount) {

        userAccount.setPassword(bCryptPasswordEncoder.encode(userAccount.getPassword()));

        userAccountRepository.save(userAccount);
    }


}
