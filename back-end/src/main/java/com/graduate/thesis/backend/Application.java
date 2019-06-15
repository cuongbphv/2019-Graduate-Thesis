package com.graduate.thesis.backend;

import com.graduate.thesis.backend.config.FileStorageProperties;
import com.graduate.thesis.backend.entity.Notification;
import com.graduate.thesis.backend.model.request.advertising.AdsMetadata;
import com.graduate.thesis.backend.model.response.AddressResponse;
import com.graduate.thesis.backend.model.response.ClassifiedAdvertisingPagingResponse;
import com.graduate.thesis.backend.model.websocket.ChatMessage;
import com.graduate.thesis.backend.repository.aggregation.AddressAggregation;
import com.graduate.thesis.backend.repository.aggregation.CategoryAggregation;
import com.graduate.thesis.backend.repository.aggregation.ChatAggregation;
import com.graduate.thesis.backend.service.NotificationService;
import com.graduate.thesis.backend.service.elastic.ClassifiedAdvertisingElasticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import static com.graduate.thesis.backend.service.NotificationService.NOTIFICATION_DESTINATION;

@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties({
        FileStorageProperties.class
})
public class Application implements CommandLineRunner {

    @Autowired
    CategoryAggregation categoryAggregation;

    @Autowired
    AddressAggregation addressAggregation;

    @Autowired
    ChatAggregation chatAggregation;

    @Autowired
    NotificationService notificationService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    ClassifiedAdvertisingElasticService classifiedAdvertisingElasticService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Scheduled(fixedDelay = 5000)
//    public void pushMessage(){
//        System.out.println("push");
//        Notification notification = notificationService.findNotificationById("5d02c6f040480ef774f6b48c");
//
//        simpMessagingTemplate.convertAndSendToUser(
//                "5cae4c886bf46d0b5453a66f", NOTIFICATION_DESTINATION, notification);
//
//    }

    @Override
    public void run(String... args) throws Exception {

    }
}
