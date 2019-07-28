package com.graduate.thesis.backend.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Huy Pham
 */

@Getter
@Setter
public class ChangePasswordRequest {

    private String currentPassword;

    @NotNull
    @NotBlank
    private String newPassword;

}
