package com.graduate.thesis.backend.model.response;

import com.graduate.thesis.backend.entity.Permission;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author cuongbphv created on 20/05/2019
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponse {

    private String id;

    private String name;

    private String description;

    private List<Permission> permissions;

    private int status;
}
