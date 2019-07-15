package com.graduate.thesis.backend.model.response.sysAdmin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @author cuongbphv created on 21/05/2019
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SysUserResponse {

    private String userId;

    private String phone;

    private String email;

    private String provider;

    private String roleId;

    private List<String> personalPermissions;

    private int status;

    private String firstName;

    private String lastName;

    private int type;

    private Date createdDate;

}
