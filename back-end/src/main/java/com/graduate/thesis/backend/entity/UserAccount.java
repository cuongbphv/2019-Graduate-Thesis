package com.graduate.thesis.backend.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
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

    private String password;

    private String roleId;

    private String[] personalPermissions;

}
