package com.graduate.thesis.backend.repository.aggregation;

import com.graduate.thesis.backend.entity.Category;
import com.graduate.thesis.backend.model.data.IdsResponse;
import com.graduate.thesis.backend.model.response.CategoryResponse;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.VariableOperators.mapItemsOf;

/**
 * @author Huy Pham
 */

@Repository
public class CategoryAggregation {

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
                match(Criteria.where("_id").is(id).and("status").is(1)),
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

    public IdsResponse getSubIdByParentId(String parentId){


        GraphLookupOperation graphLookupOperation =
                graphLookup("category")
                        .startWith("$_id")
                        .connectFrom("_id")
                        .connectTo("parentId")
                        .maxDepth(20)
                        .as("subs");

        ProjectionOperation projectionOperation =
                project().andExclude("_id")
                        .and(mapItemsOf("$subs").
                                as("subIds").
                                andApply(new AggregationExpression() {
                                    @Override
                                    public Document toDocument(AggregationOperationContext aggregationOperationContext) {
                                        return new Document("$toString", "$$subIds._id");
                                    }
                                }))
                        .as("ids");


        return mongoTemplate.aggregate(Aggregation.newAggregation(
                match(Criteria.where("_id").is(parentId).and("status").is(1)),
                graphLookupOperation,
                projectionOperation
        ), "category", IdsResponse.class).getUniqueMappedResult();
    }

    public void deleteCategoryByIds(List<String> ids){

        Query query = new Query();
        query.addCriteria(Criteria.where("_id").in(ids));
        Update update = new Update();
        update.set("status", "0");
        mongoTemplate.updateMulti(query, update, Category.class);

    }

}
