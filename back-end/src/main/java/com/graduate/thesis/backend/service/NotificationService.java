package com.graduate.thesis.backend.service;

import com.graduate.thesis.backend.entity.Notification;
import com.graduate.thesis.backend.model.websocket.NotificationResponse;

import java.util.List;

/**
 * @author Huy Pham
 */

public interface NotificationService {

    String NOTIFICATION_DESTINATION = "/queue/notification";

    void broadCastToUsers(NotificationResponse message, List<String> userIds);

    Notification save(Notification conversation);

    Notification findNotificationById(String id);

    NotificationResponse getNotificationResponseById(String id);

    List<NotificationResponse> getNotificationsByUserId(String userId, int size);

}
