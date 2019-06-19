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
public class ChatResponse {

    private String conversationId;

    private Object data;

    private Type type;

    public enum Type {
        CONVERSATION,
        MESSAGE
    }
}
