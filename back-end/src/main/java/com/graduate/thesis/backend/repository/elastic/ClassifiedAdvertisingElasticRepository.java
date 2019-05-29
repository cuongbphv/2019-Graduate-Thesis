package com.graduate.thesis.backend.repository.elastic;

import com.graduate.thesis.backend.entity.elastic.ClassifiedAdvertisingElastic;
import com.graduate.thesis.backend.model.data.ClassifiedAdvertisingElasticPagingResponse;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.client.JestResultHandler;
import io.searchbox.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Huy Pham
 */

@Repository
public class ClassifiedAdvertisingElasticRepository {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Value("${elastic.classified-ads.index}")
    private String classifiedAdsIndex;

    @Value("${elastic.classified-ads.type}")
    private String classifiedAdsType;

    @Autowired
    public JestClient jestClient;

    public void index(ClassifiedAdvertisingElastic classifiedAdvertisingElastic){

        Index index = new Index.Builder(classifiedAdvertisingElastic)
                .index(classifiedAdsIndex)
                .type(classifiedAdsType)
                .id(classifiedAdvertisingElastic.getId())
                .build();

        jestClient.executeAsync(index, new JestResultHandler<JestResult>() {
            @Override
            public void completed(JestResult result) {
                LOGGER.info("index success: " + result.getJsonString());
            }

            @Override
            public void failed(Exception e) {
                LOGGER.error("index error: " + e.getMessage());
            }
        });
    }


    public JestResult getById(String id) throws IOException {
        Get get = new Get.Builder(classifiedAdsIndex, id)
                .type(classifiedAdsType)
                .build();

        return jestClient.execute(get);
    }



    public void delete(String id) {
        Delete indexToBeDeleted = new Delete.Builder(id)
                .index(classifiedAdsIndex)
                .type(classifiedAdsType)
                .build();
        jestClient.executeAsync(indexToBeDeleted, new JestResultHandler<DocumentResult>() {
            @Override
            public void completed(DocumentResult documentResult) {
                LOGGER.info("delete success with id: " + id);
            }

            @Override
            public void failed(Exception e) {
                LOGGER.error("delete error " + e.getMessage());
            }
        });
    }


    public ClassifiedAdvertisingElasticPagingResponse executeSearch(String query) {

        int totalRecord = 0;

        ClassifiedAdvertisingElasticPagingResponse response = new ClassifiedAdvertisingElasticPagingResponse();

        Search search = new Search.Builder(query)
                // multiple index or types can be added.
                .addIndex(classifiedAdsIndex)
                .addType(classifiedAdsType)
                .build();
        //ESResultList resultList = new ESResultList<>();
        try {
            SearchResult result = jestClient.execute(search);

            List<ClassifiedAdvertisingElastic> classifiedAdvertisingElasticList = new ArrayList<>();

            List<SearchResult.Hit<ClassifiedAdvertisingElastic, Void>> hits = result.getHits(ClassifiedAdvertisingElastic.class);
            response.setTotalRecord(result.getTotal());
            for (SearchResult.Hit hit : hits) {
                ClassifiedAdvertisingElastic source = (ClassifiedAdvertisingElastic) hit.source;
                classifiedAdvertisingElasticList.add(source);
            }
            response.setItems(classifiedAdvertisingElasticList);

        } catch (Exception ex) {
            LOGGER.error("Classified Ads Repo, executeSearch(), e", ex);
        }
        return response;

    }

}
