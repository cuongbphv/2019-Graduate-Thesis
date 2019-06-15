package com.graduate.thesis.backend.service;

import com.graduate.thesis.backend.entity.Notification;
import com.graduate.thesis.backend.model.websocket.NotificationResponse;
import com.graduate.thesis.backend.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Huy Pham
 */

@Service
public class NotificationServiceImpl implements NotificationService {


    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public void broadCastToUsers(NotificationResponse message, List<String> userIds) {

        for(String userId : userIds) {
            simpMessagingTemplate.convertAndSendToUser(
                    userId, NOTIFICATION_DESTINATION, message);
        }
    }

    @Override
    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public Notification findNotificationById(String id) {
        return notificationRepository.findNotificationById(id);
    }

    @Override
    public NotificationResponse getNotificationResponseById(String id) {
        return null;
    }

    @Override
    public List<NotificationResponse> getNotificationsByUserId(String userId, int size) {
        return null;
    }
}
