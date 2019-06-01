package com.graduate.thesis.backend.repository.aggregation;

import com.graduate.thesis.backend.entity.Address;
import com.graduate.thesis.backend.entity.Location;
import com.graduate.thesis.backend.model.response.AddressResponse;
import com.graduate.thesis.backend.repository.aggregation.operation.CustomProjectAggregationOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * @author Huy Pham
 */
@Service
public class AddressAggregation {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public AddressAggregation(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<AddressResponse> getListAddressByUserId(String userId){

        String lookUpQuery =
                "{" +
                        "      $lookup:" +
                        "         {" +
                        "           from: 'location'," +
                        "           let: { province_id: '$provinceId', district_id: '$districtId', ward_id: '$wardId' }," +
                        "           pipeline: [" +
                        "           { $match:" +
                        "                 { $expr:" +
                        "                    {" +
                        "                         $eq: [ '$_id',  '$$province_id' ]" +
                        "                    }" +
                        "                 }" +
                        "           }," +
                        "           { $unwind : '$districts' }," +
                        "           { $match:" +
                        "                 { $expr:" +
                        "                    {" +
                        "                         $eq: [ '$districts._id',  '$$district_id' ]" +
                        "                    }" +
                        "                 }" +
                        "           }," +
                        "           { $unwind : '$districts.wards' }," +
                        "           { $match:" +
                        "                 { $expr:" +
                        "                    {" +
                        "                         $eq: [ '$districts.wards._id',  '$$ward_id' ]" +
                        "                    }" +
                        "                 }" +
                        "           }" +
                        "         " +
                        "           ]," +
                        "           as: 'location'" +
                        "         }" +
                        "    }";


        String projectQuery =
                " {" +
                        "                $project: { province: { $arrayElemAt: [ '$location', 0 ] } ," +
                        "                            district: { $arrayElemAt: [ '$location.districts', 0 ] } , " +
                        "                            ward: { $arrayElemAt: [ '$location.districts.wards', 0 ] } " +
                        "                            detail: 1 " +
                        "                            } " +
                        "            }";

        String filterProjectQuery = " {" +
                "                $project: { 'province.districts': 0 ,'district.wards': 0} " +
                "            }";

        TypedAggregation<AddressResponse> aggregation = Aggregation.newAggregation(
                AddressResponse.class,
                match(Criteria.where("userId").is(userId).and("status").is(1)),
                new CustomProjectAggregationOperation(lookUpQuery),
                new CustomProjectAggregationOperation(projectQuery),
                new CustomProjectAggregationOperation(filterProjectQuery)
        );

        AggregationResults<AddressResponse> results =
                mongoTemplate.aggregate(aggregation,"address" , AddressResponse.class);

        return results.getMappedResults();
    }

    public AddressResponse getAddressByAddressIdAndUserId(String addressId, String userId){

        String lookUpQuery =
                "{" +
                        "      $lookup:" +
                        "         {" +
                        "           from: 'location'," +
                        "           let: { province_id: '$provinceId', district_id: '$districtId', ward_id: '$wardId' }," +
                        "           pipeline: [" +
                        "           { $match:" +
                        "                 { $expr:" +
                        "                    {" +
                        "                         $eq: [ '$_id',  '$$province_id' ]" +
                        "                    }" +
                        "                 }" +
                        "           }," +
                        "           { $unwind : '$districts' }," +
                        "           { $match:" +
                        "                 { $expr:" +
                        "                    {" +
                        "                         $eq: [ '$districts._id',  '$$district_id' ]" +
                        "                    }" +
                        "                 }" +
                        "           }," +
                        "           { $unwind : '$districts.wards' }," +
                        "           { $match:" +
                        "                 { $expr:" +
                        "                    {" +
                        "                         $eq: [ '$districts.wards._id',  '$$ward_id' ]" +
                        "                    }" +
                        "                 }" +
                        "           }" +
                        "         " +
                        "           ]," +
                        "           as: 'location'" +
                        "         }" +
                        "    }";

        String projectQuery =
                " {" +
                        "                $project: { province: { $arrayElemAt: [ '$location', 0 ] } ," +
                        "                            district: { $arrayElemAt: [ '$location.districts', 0 ] } , " +
                        "                            ward: { $arrayElemAt: [ '$location.districts.wards', 0 ] } " +
                        "                            detail: 1 " +
                        "                            } " +
                        "            }";

        String filterProjectQuery = " {" +
                "                $project: { 'province.districts': 0 ,'district.wards': 0} " +
                "            }";

        TypedAggregation<AddressResponse> aggregation = Aggregation.newAggregation(
                AddressResponse.class,
                match(Criteria.where("userId").is(userId).and("_id").is(addressId).and("status").is(1)),
                new CustomProjectAggregationOperation(lookUpQuery),
                new CustomProjectAggregationOperation(projectQuery),
                new CustomProjectAggregationOperation(filterProjectQuery)
        );

        AggregationResults<AddressResponse> results =
                mongoTemplate.aggregate(aggregation,"address" , AddressResponse.class);

        return results.getUniqueMappedResult();
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


    public void deleteAddressByIds(List<String> ids){

        Query query = new Query();
        query.addCriteria(Criteria.where("_id").in(ids));
        Update update = new Update();
        update.set("status", "0");
        mongoTemplate.updateMulti(query, update, Address.class);

    }

}
