package com.graduate.thesis.backend.model.request.role_permission;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author cuongbphv created on 20/05/2019
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleRequest {

    private String id;

    @NotNull
    @NotBlank
    private String name;

    private String description;

    @NotNull
    private List<String> permissionIds;
}
