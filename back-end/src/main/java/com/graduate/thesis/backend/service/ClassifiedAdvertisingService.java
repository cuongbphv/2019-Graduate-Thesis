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

    Page<ClassifiedAdvertising> getPagingNewAds(String searchKey, int sortKey, boolean ascSort,
                                                int pageNumber, int pageSize, List<Integer> status);
}
