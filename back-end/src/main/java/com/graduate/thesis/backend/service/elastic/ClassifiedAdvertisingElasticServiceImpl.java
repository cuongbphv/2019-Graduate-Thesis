package com.graduate.thesis.backend.service.elastic;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.graduate.thesis.backend.entity.ClassifiedAdvertising;
import com.graduate.thesis.backend.entity.elastic.ClassifiedAdvertisingElastic;
import com.graduate.thesis.backend.model.request.advertising.AdsMetadata;
import com.graduate.thesis.backend.model.data.ClassifiedAdvertisingElasticPagingResponse;
import com.graduate.thesis.backend.model.response.ClassifiedAdvertisingPagingResponse;
import com.graduate.thesis.backend.repository.elastic.ClassifiedAdvertisingElasticRepository;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Huy Pham
 */

@Service
public class ClassifiedAdvertisingElasticServiceImpl implements ClassifiedAdvertisingElasticService {

    @Autowired
    ClassifiedAdvertisingElasticRepository advertisingElasticRepository;


    @Override
    public void index(ClassifiedAdvertisingElastic classifiedAdvertisingElastic) {
        advertisingElasticRepository.index(classifiedAdvertisingElastic);
    }

    @Override
    public void delete(String id) {
        advertisingElasticRepository.delete(id);
    }

    @Override
    public ClassifiedAdvertisingElastic getById(String id) {
        return null;
    }

    @Override
    public ClassifiedAdvertisingPagingResponse fullTextSearch(
            String categoryId, String searchKey, String locationId, Map<String,String> filterData, int pageNumber,
            int pageSize, String sortCase, boolean ascSort, double minPrice, double maxPrice) {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();


        if(searchKey != null && !searchKey.isEmpty()) {


            boolQueryBuilder.should(
                    QueryBuilders
                            .matchQuery("additionalInfo.title", searchKey)
                            .boost(5));

            boolQueryBuilder.should(
                    QueryBuilders
                            .nestedQuery(
                                    "metadata",
                                    QueryBuilders.matchQuery("metadata.valueLabel", searchKey).boost(3),
                                    ScoreMode.None)
            );


            boolQueryBuilder.should(
                    QueryBuilders
                            .nestedQuery(
                                    "metadata",
                                    QueryBuilders.matchQuery("metadata.enValueLabel", searchKey).boost(3),
                                    ScoreMode.None)
            );


            boolQueryBuilder.should(
                    QueryBuilders
                            .matchQuery("additionalInfo.description", searchKey)
                            .boost(4));

            boolQueryBuilder.should(
                    QueryBuilders
                            .multiMatchQuery(searchKey,
                                    "address.province.name",
                                    "address.district.name",
                                    "address.ward.name")
                            .boost(2)
            );

            boolQueryBuilder.should(
                    QueryBuilders
                            .multiMatchQuery(searchKey,
                                    "author.firstName",
                                    "author.lastName")
                            .boost(1)
            );

            boolQueryBuilder.minimumShouldMatch(1);
        }


        if(categoryId != null && !categoryId.isEmpty()) {
            boolQueryBuilder.filter(
                    QueryBuilders.termsQuery("breadcrumbs", categoryId)
            );
        }


        boolQueryBuilder.filter(
                QueryBuilders
                        .rangeQuery("additionalInfo.price")
                        .gte(minPrice).lte(maxPrice)
        );

        if(locationId != null && !locationId.isEmpty()){

            if(locationId.length() == 2){
                boolQueryBuilder.filter(
                        QueryBuilders
                                .termQuery("address.province.id", locationId)
                );
            }
            else if(locationId.length() == 3){
                boolQueryBuilder.filter(
                        QueryBuilders
                                .termQuery("address.district.id", locationId)
                );
            }
            else if(locationId.length() == 5){
                boolQueryBuilder.filter(
                        QueryBuilders
                                .termQuery("address.ward.id", locationId)
                );
            }
        }


//        for(AdsMetadata metadata : filterData){
//
//            if(metadata.getType().equals("text") || metadata.getType().equals("color")){
//                boolQueryBuilder.filter(
//                        QueryBuilders
//                                .nestedQuery(
//                                        "metadata",
//                                        QueryBuilders.matchQuery("metadata.value" ,metadata.getValue()),
//                                        ScoreMode.None)
//                );
//            }
//            else if(metadata.getType().equals("range")){
//                boolQueryBuilder.filter(
//                        QueryBuilders
//                                .nestedQuery(
//                                        "metadata",
//                                        QueryBuilders.rangeQuery("metadata.value")
//                                                .gte(Double.parseDouble(metadata.getValue().split("-")[0]))
//                                                .lte(Double.parseDouble(metadata.getValue().split("-")[1])),
//                                        ScoreMode.None)
//                );
//            }

        filterData.forEach((key, value) -> {

            key = key.replaceAll("_", "-");

//            if (value.contains("-")) {
//
//                boolQueryBuilder.filter(
//                        QueryBuilders
//                                .nestedQuery(
//                                        "metadata",
//                                        QueryBuilders.rangeQuery("metadata.value")
//                                                .gte(Double.parseDouble(value.split("-")[0]))
//                                                .lte(Double.parseDouble(value.split("-")[1])),
//                                        ScoreMode.None)
//                );
//            }
//            else
            if(value.contains(",")){

                boolQueryBuilder.filter(
                        QueryBuilders
                                .nestedQuery(
                                        "metadata",
                                        QueryBuilders
                                                .matchQuery("metadata.valueLabel",
                                                        value.replaceAll(",", " "))
                                                .operator(Operator.AND),
                                        ScoreMode.None)
                );

//                boolQueryBuilder.should(
//                        QueryBuilders
//                                .nestedQuery(
//                                        "metadata",
//                                        QueryBuilders.matchQuery("metadata.value", value.replaceAll(",", " ")),
//                                        ScoreMode.Total)
//                );

//                List<String> values = Lists.newArrayList(Splitter.on(",").split(value));
//
//                boolQueryBuilder.filter(
//                        QueryBuilders
//                                .nestedQuery(
//                                        "metadata",
//                                        QueryBuilders.termsQuery("metadata.value", values),
//                                        ScoreMode.None)
//                );
            }
            else {
                boolQueryBuilder.filter(
                        QueryBuilders
                                .nestedQuery(
                                        "metadata",
                                        QueryBuilders.matchQuery("metadata.valueLabel", value)
                                                .operator(Operator.AND),
                                        ScoreMode.None)
                );
            }

            boolQueryBuilder.filter(
                    QueryBuilders
                            .nestedQuery(
                                    "metadata",
                                    QueryBuilders.matchQuery("metadata.slug" ,key),
                                    ScoreMode.None)
            );
        });


        if(!sortCase.equals("_score")) {
            FieldSortBuilder sortTerm = SortBuilders.fieldSort(sortCase).order(ascSort ? SortOrder.ASC : SortOrder.DESC);
            searchSourceBuilder
                    .sort(sortTerm)
                    .sort(SortBuilders.fieldSort("createdDate").order(SortOrder.DESC))
                    .sort(SortBuilders.fieldSort("_score").order(SortOrder.DESC));
        }
        else {
            FieldSortBuilder sortTerm = SortBuilders.fieldSort(sortCase).order(ascSort ? SortOrder.ASC : SortOrder.DESC);
            searchSourceBuilder
                    .sort(SortBuilders.fieldSort("_score").order(SortOrder.DESC))
                    .sort(SortBuilders.fieldSort("createdDate").order(SortOrder.DESC));
        }

        searchSourceBuilder
                .query(boolQueryBuilder)
                .from(pageNumber * pageSize)
                .size(pageSize);


        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(searchSourceBuilder.toString()).getAsJsonObject();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPretty = gson.toJson(json);

        ClassifiedAdvertisingElasticPagingResponse queryResult = advertisingElasticRepository.executeSearch(jsonPretty);

//        List<ClassifiedAdvertising> classifiedAdvertisingList = queryResult.getContent()
//                .stream()
//                .map(ClassifiedAdvertising::new)
//                .collect(Collectors.toList());

        ClassifiedAdvertisingPagingResponse response = new ClassifiedAdvertisingPagingResponse();
        response.setTotalRecord(queryResult.getTotalRecord());
        response.setContent(queryResult.getContent());
        response.setPageNumber(pageNumber + 1);
        response.setPageSize(pageSize);

        return  response;
    }


}