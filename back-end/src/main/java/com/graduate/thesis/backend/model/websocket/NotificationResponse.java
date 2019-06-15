package com.graduate.thesis.backend.model.websocket;

import com.graduate.thesis.backend.entity.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @author Huy Pham
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {

    private String id;

    private UserProfile user;

    private UserProfile sender;

    private Object data;

    private Date createdDate;

    private Type type;

    public enum Type {
        NEW_POST,
        FOLLOW
    }

}
