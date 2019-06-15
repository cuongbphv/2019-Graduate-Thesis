package com.graduate.thesis.backend.controller;

import com.graduate.thesis.backend.entity.Conversation;
import com.graduate.thesis.backend.exception.ApplicationException;
import com.graduate.thesis.backend.model.response.ConversationResponse;
import com.graduate.thesis.backend.model.response.RestAPIResponse;
import com.graduate.thesis.backend.model.websocket.ChatMessage;
import com.graduate.thesis.backend.model.websocket.ChatResponse;
import com.graduate.thesis.backend.security.CurrentUser;
import com.graduate.thesis.backend.security.oauth2.user.UserPrincipal;
import com.graduate.thesis.backend.service.ChatService;
import com.graduate.thesis.backend.util.APIStatus;
import com.graduate.thesis.backend.util.CommonUtil;
import com.graduate.thesis.backend.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Huy Pham
 */

@RestController
@RequestMapping(Constant.CHAT_API)
public class ChatController extends AbstractBasedAPI{

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private ChatService chatService;


    @GetMapping()
    public ResponseEntity<RestAPIResponse> getAllConversations(
            @RequestParam("user_id") String userId,
            @RequestParam(value = "size", defaultValue = "10") int size
            ) {

        List<ConversationResponse> existedConversation = chatService.findConversationByUserId(userId, size);

        return responseUtil.successResponse(existedConversation);
    }

    @GetMapping(Constant.WITHIN_ID)
    public ResponseEntity<RestAPIResponse> getConversation(
            @PathVariable("id") String conversationId) {

        ConversationResponse existedConversation = chatService.getConversationDataById(conversationId);

        if(existedConversation == null){
            throw new ApplicationException(APIStatus.ERR_CONVERSATION_NOT_EXISTED);
        }

        return responseUtil.successResponse(existedConversation);
    }


    @PostMapping
    public ResponseEntity<RestAPIResponse> createConversation(
            @CurrentUser UserPrincipal currentUser,
            @RequestParam("user_id") String userId) {

        List<String> members = new ArrayList<>();
        members.add(currentUser.getId());
        members.add(userId);

        Conversation existedConversation = chatService.findConversationByMembers(members);

        if(existedConversation != null){
            throw new ApplicationException(APIStatus.ERR_CONVERSATION_EXISTED);
        }

        Conversation conversation = new Conversation();
        conversation.setMembers(members);
        conversation.setCreatedDate(new Date());

        Conversation createdConversation = chatService.save(conversation);

        //broadcast to member
        ChatResponse chatResponse = new ChatResponse();
        chatResponse.setConversationId(createdConversation.getId());
        chatResponse.setType(ChatResponse.Type.CONVERSATION);
        chatResponse.setData(createdConversation);
        chatService.broadCastToUsers(chatResponse, members);

        return responseUtil.successResponse(createdConversation);
    }

    @GetMapping(Constant.WITHIN_ID + Constant.MESSAGE)
    public ResponseEntity<RestAPIResponse> getMessageHistory(
            @PathVariable("id") String conversationId,
            @RequestParam(value = "size", defaultValue = "30") int size
            ) {

        List<ChatMessage> messages = chatService.getMessageHistory(conversationId, size);

        return responseUtil.successResponse(messages);
    }

    @PostMapping(Constant.WITHIN_ID + Constant.MESSAGE)
    public ResponseEntity<RestAPIResponse> sendMessage(@RequestBody ChatMessage chatMessage,
                            @PathVariable("id") String conversationId) {

        Conversation existedConversation = chatService.findConversationById(conversationId);

        if(existedConversation == null){
            throw new ApplicationException(APIStatus.ERR_CONVERSATION_NOT_EXISTED);
        }

        chatMessage.setMessageId(CommonUtil.generateUUID());
        // escape html
        chatMessage.setContent(chatMessage.getContent());
        chatMessage.setCreatedDate(new Date());
        chatMessage.setStatus(ChatMessage.MessageStatus.SENT);
        chatService.addMessage(chatMessage, conversationId);

        //broadcast to member
        ChatResponse chatResponse = new ChatResponse();
        chatResponse.setConversationId(existedConversation.getId());
        chatResponse.setType(ChatResponse.Type.MESSAGE);
        chatResponse.setData(chatMessage);
        chatService.broadCastToUsers(chatResponse, existedConversation.getMembers());

        return responseUtil.successResponse(chatMessage);
    }
}
