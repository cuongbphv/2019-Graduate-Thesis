package com.graduate.thesis.backend.controller;

import com.graduate.thesis.backend.entity.UserAccount;
import com.graduate.thesis.backend.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * @author Huy Pham
 */

@RestController
public class TestController {

    @PreAuthorize("hasAnyAuthority('CREATE_POST', 'VIEW_POST')")
    @RequestMapping(value = "/greeting", method = RequestMethod.GET)
    public String greeting(){
        return "Hi, Welcome to 2019";
    }



//    @RequestMapping(value = "/login/oauth2/code/google", method = RequestMethod.GET)
//    public String greeting1(){
//        return "Hi, Welcome to 2019";
//    }
}
