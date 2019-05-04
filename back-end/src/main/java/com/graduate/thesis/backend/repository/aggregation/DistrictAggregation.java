package com.graduate.thesis.backend.repository.aggregation;

import com.graduate.thesis.backend.entity.Location;
import com.graduate.thesis.backend.entity.model.District;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * @author cuongbphv created on 29/04/2019
 */
@Repository
public class DistrictAggregation {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public DistrictAggregation(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<District> getDistrictByProvinceId(String provinceId, String searchKey, int sortKey, boolean ascSort) {
        SortOperation sortOperation = buildSortOperation(sortKey, ascSort);
        GroupOperation groupOperation = buildGroupOperation();
        ProjectionOperation projectOperation = buildProjectOperation();

        return mongoTemplate.aggregate(Aggregation.newAggregation(
                match(Criteria.where("_id").is(provinceId)),
                unwind("$districts"),
                sortOperation,
                groupOperation,
                projectOperation
        ), Location.class, District.class).getMappedResults();
    }

    private SortOperation buildSortOperation(int sortKey, boolean ascSort) {
        String properties = "";

        switch (sortKey) {
            case 1:
                properties = "districts._id"; break;
            case 2:
                properties = "districts.name"; break;
            case 3:
                properties = "districts.type"; break;
            default:
                properties = "districts._id";
        }
        return sort(ascSort ? Sort.Direction.ASC : Sort.Direction.DESC, properties);
    }

    private GroupOperation buildGroupOperation() {
        return group("_id")
                .push("$districts").as("districts");
    }

    private ProjectionOperation buildProjectOperation() {
        return project(bind("_id", "$districts._id").and("name", "$district.name"));
    }
}

