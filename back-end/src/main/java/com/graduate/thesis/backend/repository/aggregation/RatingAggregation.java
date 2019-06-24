package com.graduate.thesis.backend.repository.aggregation;

import com.graduate.thesis.backend.entity.Rating;
import com.graduate.thesis.backend.model.response.RatingResponse;
import com.graduate.thesis.backend.repository.aggregation.operation.CustomProjectAggregationOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;

/**
 * @author Huy Pham
 */

@Repository
public class RatingAggregation {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<RatingResponse> getByRecipientId(String recipientId, int size){

        String lookupQuery =
                "{ $lookup:\n" +
                        "        {\n" +
                        "            from: 'user_profile',\n" +
                        "            localField: 'senderId',\n" +
                        "            foreignField: 'userId',\n" +
                        "            as: 'sender'\n" +
                        "        } \n" +
                        "    }";

        String unwindQuery =
                "{ $unwind: '$sender' }";

        String projectQuery =
               "{ $project: { senderId: 0 } }";

        String sortQuery =
                "{ $sort : { createdDate : -1 } }";

        return mongoTemplate.aggregate(Aggregation.newAggregation(
                match(Criteria.where("recipientId").is(recipientId)),
                new CustomProjectAggregationOperation(lookupQuery),
                new CustomProjectAggregationOperation(unwindQuery),
                new CustomProjectAggregationOperation(projectQuery),
                new CustomProjectAggregationOperation(sortQuery),
                limit(size)
                ),
                Rating.class, RatingResponse.class).getMappedResults();
    }

}
