package com.graduate.thesis.backend.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Huy Pham
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RatingRequest {

    private String senderId;

    private String recipientId;

    private String title;

    private int value;

    private String comment;

}
