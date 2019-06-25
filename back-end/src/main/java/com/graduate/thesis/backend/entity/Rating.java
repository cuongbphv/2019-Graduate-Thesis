package com.graduate.thesis.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author Huy Pham
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document("rating")
public class Rating {

    private String id;

    private String senderId;

    private String recipientId;

    private int value;

    private String title;

    private String comment;

    private Date createdDate;

    private Date modifiedDate;

}
