package com.graduate.thesis.backend.service;

import com.graduate.thesis.backend.entity.ClassifiedAdvertising;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author cuongbphv created on 15/05/2019
 */
public interface ClassifiedAdvertisingService {

    ClassifiedAdvertising save(ClassifiedAdvertising classifiedAdvertising);

    ClassifiedAdvertising getClassifiedAdsDetail(String id, List<Integer> status);

    ClassifiedAdvertising getClassifiedAdsDetailByIdAndAuthor(String id, String authorId);

    Page<ClassifiedAdvertising> getPagingNewAds(
            String searchKey,
            String provinceId,
            String districtId,
            String wardId,
            String categoryId,
            int sortKey,
            boolean ascSort,
            int pageNumber,
            int pageSize,
            List<Integer> status
    );

    Page<ClassifiedAdvertising> getPagingAdsByAuthorId(
            String authorId,
            String searchKey,
            String provinceId,
            String districtId,
            String wardId,
            String categoryId,
            boolean ascSort,
            int pageNumber,
            int pageSize,
            List<Integer> status
    );
}
