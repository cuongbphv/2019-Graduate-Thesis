package com.graduate.thesis.backend.service;

import com.graduate.thesis.backend.entity.Conversation;
import com.graduate.thesis.backend.model.response.ConversationResponse;
import com.graduate.thesis.backend.model.websocket.ChatMessage;
import com.graduate.thesis.backend.model.websocket.ChatResponse;
import com.graduate.thesis.backend.repository.ChatRepository;
import com.graduate.thesis.backend.repository.aggregation.ChatAggregation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Huy Pham
 */

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatAggregation chatAggregation;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void broadCastToUsers(ChatResponse message, List<String> userIds) {

        for(String userId : userIds) {

            simpMessagingTemplate.convertAndSendToUser(
                            userId, CHAT_DESTINATION, message);
        }
    }

    @Override
    public Conversation save(Conversation conversation) {
        return chatRepository.save(conversation);
    }

    @Override
    public ConversationResponse getConversationDataById(String id) {
        return chatAggregation.getConversationData(id);
    }

    @Override
    public Conversation findConversationById(String id) {
        return chatRepository.findConversationById(id);
    }

    @Override
    public List<ConversationResponse> findConversationByUserId(String userId, int size) {
        return chatAggregation.getAllConversationData(userId, size);
    }

    @Override
    public Conversation findConversationByMembers(List<String> members) {
        return chatRepository.findByMembersEquals(members);
    }

    @Override
    public void addMessage(ChatMessage message, String conversationId) {
        chatAggregation.addMessage(message, conversationId);
    }

    @Override
    public void updateMessageContent(ChatMessage message, String conversationId) {
        chatAggregation.updateMessageContent(message,conversationId);
    }

    @Override
    public void updateMessageStatus(ChatMessage message, String conversationId) {
        chatAggregation.updateMessageStatus(message,conversationId);
    }

    @Override
    public List<ChatMessage> getMessageHistory(String conversationId, int size) {
        return chatAggregation.getMessageHistoryByConversationIdAndSize(conversationId, size);
    }

}
