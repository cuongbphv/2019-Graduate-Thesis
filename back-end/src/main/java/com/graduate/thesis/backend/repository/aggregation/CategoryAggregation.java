package com.graduate.thesis.backend.repository.aggregation;

import com.graduate.thesis.backend.entity.Category;
import com.graduate.thesis.backend.entity.Location;
import com.graduate.thesis.backend.entity.model.District;
import com.graduate.thesis.backend.entity.model.Metadata;
import com.graduate.thesis.backend.entity.model.MetadataOption;
import com.graduate.thesis.backend.model.response.CategoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GraphLookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.graphLookup;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;

/**
 * @author Huy Pham
 */

@Repository
public class CategoryAggregation {

    @Autowired
    private final MongoTemplate mongoTemplate;

    @Autowired
    public CategoryAggregation(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public CategoryResponse getCategoryById(String id){

        GraphLookupOperation graphLookupOperation =
                graphLookup("category")
                        .startWith("$_id")
                .connectFrom("_id")
                .connectTo("parentId")
                        .maxDepth(0)
                .as("subs");

        return mongoTemplate.aggregate(Aggregation.newAggregation(
                match(Criteria.where("_id").is(id)),
                graphLookupOperation
        ), "category", CategoryResponse.class).getUniqueMappedResult();
    }

    public List<CategoryResponse> getParentCategory(){

        GraphLookupOperation graphLookupOperation =
                graphLookup("category")
                        .startWith("$_id")
                        .connectFrom("_id")
                        .connectTo("parentId")
                        .maxDepth(0)
                        .as("subs");

        return mongoTemplate.aggregate(Aggregation.newAggregation(
                match(Criteria.where("parentId").is(null)),
                graphLookupOperation
        ), "category", CategoryResponse.class).getMappedResults();

    }

}
