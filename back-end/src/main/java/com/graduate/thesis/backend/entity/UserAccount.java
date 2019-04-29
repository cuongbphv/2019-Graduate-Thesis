package com.graduate.thesis.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.graduate.thesis.backend.security.AuthProvider;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Huy Pham
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document("user_account")
public class UserAccount implements Serializable {

    @Id
    private String id;

    private String phone;

    private String email;

    private String password;

    private AuthProvider provider;

    private String providerId;

    private String roleId;

    private List<String> personalPermissions;

    private int status;

}
