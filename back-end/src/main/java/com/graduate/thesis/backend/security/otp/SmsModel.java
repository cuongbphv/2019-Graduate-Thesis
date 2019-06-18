package com.graduate.thesis.backend.security.otp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author cuongbphv created on 17/06/2019
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SmsModel {
    private String phone;
    private String content;
}
