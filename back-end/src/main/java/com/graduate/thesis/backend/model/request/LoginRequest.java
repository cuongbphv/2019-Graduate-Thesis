package com.graduate.thesis.backend.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @author Huy Pham
 */

@Getter
@Setter
public class LoginRequest {

    @NotBlank
    private String phone;

    @NotBlank
    private String password;

}
