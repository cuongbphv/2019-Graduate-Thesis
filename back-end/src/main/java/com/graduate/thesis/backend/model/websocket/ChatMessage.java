package com.graduate.thesis.backend.model.websocket;

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
public class ChatMessage {

    private String messageId;

    private String senderId;

    private String content;

    private Date createdDate;

    private MessageStatus status;

    public enum MessageStatus {
        SENT,
        RECEIVED,
        SEEN,
        ERROR
    }
}
