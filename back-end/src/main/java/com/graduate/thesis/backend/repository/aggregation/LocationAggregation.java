package com.graduate.thesis.backend.repository.aggregation;

import com.graduate.thesis.backend.entity.Location;
import com.graduate.thesis.backend.model.response.AddressResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * @author cuongbphv created on 29/04/2019
 */
@Repository
public class LocationAggregation {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public LocationAggregation(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public AddressResponse getAddressByLocationId(String provinceId, String districtId, String wardId) {

        String filterQuery =
                "{ $project: { \"province.districts\" : 0 ,\"district.wards\" : 0} }";

        AggregationResults<AddressResponse> results =
                mongoTemplate.aggregate(Aggregation.newAggregation(
                        AddressResponse.class,
                        match(Criteria.where("_id").is(provinceId)),
                        unwind("districts"),
                        match(Criteria.where("districts._id").is(districtId)),
                        unwind("districts.wards"),
                        match(Criteria.where("districts.wards._id").is(wardId)),
                        project()
                            .and("$$ROOT").as("province")
                            .and("$districts").as("district")
                            .and("$districts.wards").as("ward"),
                        new CustomProjectAggregationOperation(filterQuery)
                ), Location.class, AddressResponse.class);

        return results.getUniqueMappedResult();
    }
}

