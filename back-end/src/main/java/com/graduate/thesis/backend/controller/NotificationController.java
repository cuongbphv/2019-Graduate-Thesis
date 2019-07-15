package com.graduate.thesis.backend.controller;

import com.graduate.thesis.backend.model.response.ConversationResponse;
import com.graduate.thesis.backend.model.response.RestAPIResponse;
import com.graduate.thesis.backend.model.websocket.NotificationResponse;
import com.graduate.thesis.backend.service.NotificationService;
import com.graduate.thesis.backend.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Huy Pham
 */

@RestController
@RequestMapping(Constant.NOTIFICATION_API)
public class NotificationController extends AbstractBasedAPI {

    @Autowired
    NotificationService notificationService;

    @GetMapping()
    public ResponseEntity<RestAPIResponse> getNotifications(
            @RequestParam("user_id") String userId,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {

        List<NotificationResponse> existedConversation = notificationService.getNotificationsByUserId(userId, size);

        return responseUtil.successResponse(existedConversation);
    }

}
