package com.graduate.thesis.backend.entity;

import com.graduate.thesis.backend.security.AuthProvider;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

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

    private String username;

    private String email;

    private String password;

    private AuthProvider provider;

    private String providerId;

    private String roleId;

    private String[] personalPermissions;

}