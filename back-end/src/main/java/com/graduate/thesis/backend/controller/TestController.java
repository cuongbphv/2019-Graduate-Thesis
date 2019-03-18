package com.graduate.thesis.backend.controller;

import com.graduate.thesis.backend.entity.User;
import com.graduate.thesis.backend.repository.SessionUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * @author Huy Pham
 */

@RestController
public class TestController {

    @RequestMapping(value = "/greeting", method = RequestMethod.GET)
    public String greeting(){
        return "Hi, Welcome to 2019";
    }

    @Autowired
    private SessionUserRepository sessionUserRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @PostMapping("/users/sign-up")
    public void signUp(@RequestBody User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        sessionUserRepository.save(user);
    }

}
