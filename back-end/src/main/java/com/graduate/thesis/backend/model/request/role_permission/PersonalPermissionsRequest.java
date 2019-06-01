package com.graduate.thesis.backend.model.request.role_permission;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author cuongbphv created on 25/05/2019
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonalPermissionsRequest {

    @NotBlank
    @NotNull
    private String userId;

    @NotBlank
    @NotNull
    private String roleId;

    private List<String> permissionIds;
}
