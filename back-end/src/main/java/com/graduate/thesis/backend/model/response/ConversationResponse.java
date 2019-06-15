package com.graduate.thesis.backend.model.response;

import com.graduate.thesis.backend.entity.UserProfile;
import com.graduate.thesis.backend.model.websocket.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @author Huy Pham
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConversationResponse {

    private String id;

    private List<UserProfile> members;

    private Date createdDate;

    private ChatMessage lastMessage;

}
