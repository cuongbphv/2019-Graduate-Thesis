package com.graduate.thesis.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author Huy Pham
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document("notification")
public class Notification {

    @Id
    private String id;

    private String userId;

    private String senderId;

    private Object data;

    private Date createdDate;

    private Type type;

    private Status status;

    public enum Type {
        NEW_POST,
        FOLLOW
    }

    public enum Status {
        UNREAD,
        READED
    }

}
