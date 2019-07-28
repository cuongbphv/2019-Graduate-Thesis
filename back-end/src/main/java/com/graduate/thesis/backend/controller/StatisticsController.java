package com.graduate.thesis.backend.controller;

import com.graduate.thesis.backend.model.response.RestAPIResponse;
import com.graduate.thesis.backend.service.ClassifiedAdvertisingService;
import com.graduate.thesis.backend.service.UserAccountService;
import com.graduate.thesis.backend.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author Huy Pham
 */

@RestController
@RequestMapping(Constant.STATISTICS_API)
public class StatisticsController extends AbstractBasedAPI{

    @Autowired
    ClassifiedAdvertisingService classifiedAdvertisingService;

    @Autowired
    UserAccountService userAccountService;

    @GetMapping()
    public ResponseEntity<RestAPIResponse> getStatistics(){

        HashMap<String, Long> data = new HashMap<>();

        long adsCounter = classifiedAdvertisingService.countAllPost();
        long userCounter = userAccountService.countUser();

        data.put("ads", adsCounter);
        data.put("user", userCounter);

        return responseUtil.successResponse(data);
    }

}
