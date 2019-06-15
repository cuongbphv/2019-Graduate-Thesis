package com.graduate.thesis.backend.service;

import com.graduate.thesis.backend.entity.Conversation;
import com.graduate.thesis.backend.model.response.ConversationResponse;
import com.graduate.thesis.backend.model.websocket.ChatMessage;
import com.graduate.thesis.backend.model.websocket.ChatResponse;

import java.util.List;

/**
 * @author Huy Pham
 */

public interface ChatService {

    String CHAT_DESTINATION = "/queue/chat";

    void broadCastToUsers(ChatResponse message, List<String> userIds);

    Conversation save(Conversation conversation);

    ConversationResponse getConversationDataById(String id);

    Conversation findConversationById(String id);

    List<ConversationResponse> findConversationByUserId(String userId, int size);

    Conversation findConversationByMembers(List<String> members);

    void addMessage(ChatMessage message, String conversationId);

    void updateMessageContent(ChatMessage message, String conversationId);

    void updateMessageStatus(ChatMessage message, String conversationId);

    List<ChatMessage> getMessageHistory(String conversationId, int size);
}
