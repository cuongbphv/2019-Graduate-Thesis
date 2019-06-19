package com.graduate.thesis.backend.security.otp;

import com.graduate.thesis.backend.Application;
import com.graduate.thesis.backend.exception.ApplicationException;
import com.graduate.thesis.backend.security.SecurityConstants;
import com.graduate.thesis.backend.util.APIStatus;
import com.graduate.thesis.backend.util.Constant;
import com.graduate.thesis.backend.util.UniqueID;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author cuongbphv created on 17/06/2019
 */
public class Sms4Api {

    private static final Sms4Api instance = new Sms4Api();

    private Sms4Api(){}

    public static Sms4Api getInstance(){
        return instance;
    }

    public void sendSmsOTP(String phone, String otp) {
        try {
            List<SmsModel> lstSend = new ArrayList<>();
            SmsModel smsModel = new SmsModel();
            smsModel.setPhone(phone);
            smsModel.setContent("BeeMarket OTP: " + otp);
            lstSend.add(smsModel);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", 0);
            jsonObject.put("lstSend", lstSend);

            HttpResponse<JsonNode> jsonResponse = Unirest.post(SecurityConstants.SMS_4_API)
                    .header("Content-Type", "application/json")
                    .body(jsonObject)
                    .asJson();

            System.out.println(jsonResponse.toString());

            if (!jsonResponse.getBody().getObject().get("code").toString().equals("1")) {
                throw new ApplicationException(APIStatus.ERR_IN_SENDING_OTP);
            }

        } catch (UnirestException e) {
            e.printStackTrace();
        }

    }

}
