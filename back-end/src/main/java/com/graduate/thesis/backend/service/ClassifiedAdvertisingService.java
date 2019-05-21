package com.graduate.thesis.backend.service;

import com.graduate.thesis.backend.entity.ClassifiedAdvertising;
import org.springframework.data.domain.Page;

/**
 * @author cuongbphv created on 15/05/2019
 */
public interface ClassifiedAdvertisingService {

    ClassifiedAdvertising save(ClassifiedAdvertising classifiedAdvertising);

    ClassifiedAdvertising getClassifiedAdsDetail(String id, int status);

    Page<ClassifiedAdvertising> getPagingNewAds(int pageNumber, int pageSize, int status);
}
