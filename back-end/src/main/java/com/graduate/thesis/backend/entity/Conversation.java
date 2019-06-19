package com.graduate.thesis.backend.entity;

import com.graduate.thesis.backend.model.websocket.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * @author Huy Pham
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document("chat")
public class Conversation {

    @Id
    private String id;

    private List<String> members;

    private List<ChatMessage> messages;

    private Date createdDate;

}
