package com.graduate.thesis.backend.repository;

import com.graduate.thesis.backend.entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Huy Pham
 */

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {

    @Query("{ '_id': ?0 }")
    Notification findNotificationById(String id);

}
