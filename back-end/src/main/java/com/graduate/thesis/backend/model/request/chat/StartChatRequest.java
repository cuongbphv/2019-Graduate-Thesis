package com.graduate.thesis.backend.model.request.chat;

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
public class StartChatRequest {

    private String messageId;

    private String senderId;

    private String receiverId;

    private String content;

    private Date createdDate;

    private com.graduate.thesis.backend.model.websocket.ChatMessage.MessageStatus status;

    public enum MessageStatus {
        SENT,
        RECEIVED,
        SEEN,
        ERROR
    }
}

