package com.graduate.thesis.backend.repository.aggregation;

import com.graduate.thesis.backend.entity.UserAccount;
import com.graduate.thesis.backend.model.response.sysAdmin.SysUserResponse;
import com.graduate.thesis.backend.repository.aggregation.operation.AddFieldOperation;
import com.graduate.thesis.backend.repository.aggregation.operation.CustomProjectAggregationOperation;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * @author cuongbphv created on 21/05/2019
 */
@Repository
public class SysAdminUserAggregation {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public SysAdminUserAggregation(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    private static AddFieldOperation addField(final Document document) {
        return new AddFieldOperation(document);
    }

    public List<SysUserResponse> getPagingUserInSysAdmin(
            String searchKey,
            int sortKey,
            boolean ascSort,
            int pageNumber,
            int pageSize,
            String roleId,
            String provider,
            int status
    ) {

        AddFieldOperation addField =
                SysAdminUserAggregation.addField(new Document().append("convertedId", new Document("$toString", "$_id")));

        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("user_profile").localField("convertedId").foreignField("userId").as("profile");

        // handle for filter by role or status or provider
        String matchCondition = "";
        if (!roleId.isEmpty()) {
            matchCondition += "{ $match: { \"roleId\" : \"" + roleId + "\" } }";
        }
        if (status != -1) {
            matchCondition += "{ $match: { \"status\" : " + status + " } }";
        }
        if (!provider.isEmpty()) {
            matchCondition += "{ $match: { \"provider\" : \"" + provider + "\" } }";
        }

        // handle for sort
        String property = "";
        switch (sortKey) {
            case 1:
                property = "firstName"; break;
            case 2:
                property = "lastName"; break;
            case 3:
                property = "phone"; break;
            case 4:
                property = "provider"; break;
            case 5:
                property = "roleId"; break;
            case 6:
                property = "status"; break;
            case 7:
                property = "createdDate"; break;
        }

        AggregationResults<SysUserResponse> results;

        if (matchCondition.isEmpty()) {

            results = mongoTemplate.aggregate(Aggregation.newAggregation(
                    SysUserResponse.class,
                    addField,
                    lookupOperation,
                    unwind("$profile"),
                    project()
                            .andExclude("_id")
                            .andInclude("phone", "provider", "roleId", "status", "personalPermissions")
                            .and("id").as("userId")
                            .and("$profile.firstName").as("firstName")
                            .and("$profile.lastName").as("lastName")
                            .and("$profile.type").as("type")
                            .and("$profile.createdDate").as("createdDate"),
                    match(new Criteria().orOperator(
                            where("firstName").regex(searchKey, "i"),
                            where("lastName").regex(searchKey, "i"),
                            where("phone").regex(searchKey, "i"))),
                    sort(ascSort ? Sort.Direction.ASC : Sort.Direction.DESC, property),
                    skip((long) ((pageNumber - 1) * pageSize)),
                    limit(pageSize)
            ), UserAccount.class, SysUserResponse.class);

        } else {
            results = mongoTemplate.aggregate(Aggregation.newAggregation(
                    SysUserResponse.class,
                    addField,
                    lookupOperation,
                    unwind("$profile"),
                    project()
                            .andExclude("_id")
                            .andInclude("phone", "provider", "roleId", "status", "personalPermissions")
                            .and("id").as("userId")
                            .and("$profile.firstName").as("firstName")
                            .and("$profile.lastName").as("lastName")
                            .and("$profile.type").as("type")
                            .and("$profile.createdDate").as("createdDate"),
                    match(new Criteria().orOperator(
                            where("firstName").regex(searchKey, "i"),
                            where("lastName").regex(searchKey, "i"),
                            where("phone").regex(searchKey, "i"))),
                    new CustomProjectAggregationOperation(matchCondition), // for case filter by condition
                    skip((long) ((pageNumber - 1) * pageSize)),
                    sort(ascSort ? Sort.Direction.ASC : Sort.Direction.DESC, property),
                    limit(pageSize)
            ), UserAccount.class, SysUserResponse.class);
        }

        return results.getMappedResults();
    }

}
