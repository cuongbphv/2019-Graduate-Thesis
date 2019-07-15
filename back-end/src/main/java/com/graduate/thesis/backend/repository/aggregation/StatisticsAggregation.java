package com.graduate.thesis.backend.repository.aggregation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author Huy Pham
 */

@Repository
public class StatisticsAggregation {

    @Autowired
    private MongoTemplate mongoTemplate;


}
