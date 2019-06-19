package com.graduate.thesis.backend.service.elastic;

import com.graduate.thesis.backend.entity.ClassifiedAdvertising;
import com.graduate.thesis.backend.entity.elastic.ClassifiedAdvertisingElastic;
import com.graduate.thesis.backend.entity.model.Metadata;
import com.graduate.thesis.backend.model.request.advertising.AdsMetadata;
import com.graduate.thesis.backend.model.response.ClassifiedAdvertisingPagingResponse;

import java.util.List;
import java.util.Map;

/**
 * @author Huy Pham
 */

public interface ClassifiedAdvertisingElasticService {

    void index(ClassifiedAdvertisingElastic classifiedAdvertisingElastic);

    void delete(String id);

    ClassifiedAdvertisingElastic getById(String id);

    ClassifiedAdvertisingPagingResponse fullTextSearch(
            String categoryId,
            String searchKey,
            String locationId,
            Map<String,String> filterData,
            int pageNumber,
            int pageSize,
            String sortCase,
            boolean ascSort,
            double minPrice,
            double maxPrice
    );
}
