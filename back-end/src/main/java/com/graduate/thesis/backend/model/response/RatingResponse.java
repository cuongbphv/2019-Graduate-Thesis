package com.graduate.thesis.backend.model.response;

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
public class RatingResponse {

    private String id;

    private UserProfile sender;

    private String recipientId;

    private int value;

    private String title;

    private String comment;

    private Date createdDate;

    private Date modifiedDate;

}
