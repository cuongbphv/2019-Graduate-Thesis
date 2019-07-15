package com.graduate.thesis.backend.repository.aggregation;

import com.graduate.thesis.backend.entity.Notification;
import com.graduate.thesis.backend.model.websocket.NotificationResponse;
import com.graduate.thesis.backend.repository.aggregation.operation.CustomProjectAggregationOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * @author Huy Pham
 */
@Repository
public class NotificationAggregation {

    @Autowired
    private MongoTemplate mongoTemplate;

    public NotificationResponse getNotificationDataById(String notificationId){

        String lookupQuery =
              "{ \"$lookup\": {\n" +
                      "       \"from\": \"user_profile\",\n" +
                      "       \"localField\": \"senderId\",\n" +
                      "       \"foreignField\": \"userId\",\n" +
                      "       \"as\": \"sender\"\n" +
                      "    }}";

        String unwindQuery =
                "{ \"$unwind\": \"$sender\" }";

        String projectQuery =
                "    { $project: {\n" +
                        "          _id: 1,\n" +
                        "          sender: \"$sender\",\n" +
                        "          createdDate: 1,\n" +
                        "          type: 1,\n" +
                        "          status: 1,\n" +
                        "          data: 1\n" +
                        "       } \n" +
                        "    }";


        return mongoTemplate.aggregate(Aggregation.newAggregation(
                match(Criteria.where("_id").is(notificationId)),
                new CustomProjectAggregationOperation(lookupQuery),
                new CustomProjectAggregationOperation(unwindQuery),
                new CustomProjectAggregationOperation(projectQuery)
                ),
                Notification.class, NotificationResponse.class).getUniqueMappedResult();
    }

    public List<NotificationResponse> getNotificationDataByUserId(String userId, int size){

        String matchQuery =
                "   { $match:   { receiverId: { $all: [ \"" + userId+ "\"] } } }";

        String lookupQuery =
                "{ \"$lookup\": {\n" +
                        "       \"from\": \"user_profile\",\n" +
                        "       \"localField\": \"senderId\",\n" +
                        "       \"foreignField\": \"userId\",\n" +
                        "       \"as\": \"sender\"\n" +
                        "    }}";

        String unwindQuery =
                "{ \"$unwind\": \"$sender\" }";

        String projectQuery =
                "    { $project: {\n" +
                        "          _id: 1,\n" +
                        "          sender: \"$sender\",\n" +
                        "          createdDate: 1,\n" +
                        "          type: 1,\n" +
                        "          status: 1,\n" +
                        "          data: 1\n" +
                        "       } \n" +
                        "    }";

        String sortQuery =
                "{ $sort: { createdDate: -1 } }";


        return mongoTemplate.aggregate(Aggregation.newAggregation(
                new CustomProjectAggregationOperation(matchQuery),
                new CustomProjectAggregationOperation(lookupQuery),
                new CustomProjectAggregationOperation(unwindQuery),
                new CustomProjectAggregationOperation(projectQuery),
                new CustomProjectAggregationOperation(sortQuery),
                limit(size)),
                Notification.class, NotificationResponse.class).getMappedResults();
    }

}
