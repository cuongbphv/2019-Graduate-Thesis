package com.graduate.thesis.backend.service.elastic;

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
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public ClassifiedAdvertisingPagingResponse fullTextSearch(String categoryId, String searchKey, List<AdsMetadata> filterData, int pageNumber, int pageSize) {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();


        boolQueryBuilder.should(
                QueryBuilders
                        .matchQuery("additionalInfo.title", searchKey)
                        .boost(5));

        boolQueryBuilder.should(
                QueryBuilders
                        .nestedQuery(
                                "metadata",
                                QueryBuilders.matchQuery("metadata.value", searchKey).boost(4),
                                ScoreMode.None)
        );


        boolQueryBuilder.should(
                QueryBuilders
                        .matchQuery("additionalInfo.description", searchKey)
                        .boost(3));

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


        boolQueryBuilder.filter(
                QueryBuilders.termsQuery("breadcrumbs", categoryId)
        );


        boolQueryBuilder.filter(
                QueryBuilders
                        .rangeQuery("additionalInfo.price")
                        .gte(0).lte(300000000)
        );


        for(AdsMetadata metadata : filterData){

            if(metadata.getType().equals("text") || metadata.getType().equals("color")){
                boolQueryBuilder.filter(
                        QueryBuilders
                                .nestedQuery(
                                        "metadata",
                                        QueryBuilders.matchQuery("metadata.value" ,metadata.getValue()),
                                        ScoreMode.None)
                );
            }
            else if(metadata.getType().equals("range")){
                boolQueryBuilder.filter(
                        QueryBuilders
                                .nestedQuery(
                                        "metadata",
                                        QueryBuilders.rangeQuery("metadata.value")
                                                .gte(Double.parseDouble(metadata.getValue().split("-")[0]))
                                                .lte(Double.parseDouble(metadata.getValue().split("-")[1])),
                                        ScoreMode.None)
                );
            }


            boolQueryBuilder.filter(
                    QueryBuilders
                            .nestedQuery(
                                    "metadata",
                                    QueryBuilders.matchQuery("metadata.slug" ,metadata.getSlug()),
                                    ScoreMode.None)
            );
        }


        searchSourceBuilder
                .query(boolQueryBuilder)
                .sort(SortBuilders.fieldSort("createdDate").order(SortOrder.DESC))
                .sort(SortBuilders.fieldSort("_score").order(SortOrder.DESC))
                .from(pageNumber * pageSize)
                .size(pageSize);


        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(searchSourceBuilder.toString()).getAsJsonObject();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPretty = gson.toJson(json);

        ClassifiedAdvertisingElasticPagingResponse queryResult = advertisingElasticRepository.executeSearch(jsonPretty);

        List<ClassifiedAdvertising> classifiedAdvertisingList = queryResult.getItems()
                .stream()
                .map(ClassifiedAdvertising::new)
                .collect(Collectors.toList());

        ClassifiedAdvertisingPagingResponse response = new ClassifiedAdvertisingPagingResponse();
        response.setTotalRecord(queryResult.getTotalRecord());
        response.setItems(classifiedAdvertisingList);
        response.setPageNumber(pageNumber + 1);
        response.setPageSize(pageSize);

        return  response;
    }


}
