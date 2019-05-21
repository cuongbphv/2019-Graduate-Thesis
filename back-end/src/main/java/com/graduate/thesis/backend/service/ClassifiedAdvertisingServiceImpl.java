package com.graduate.thesis.backend.service;

import com.graduate.thesis.backend.entity.ClassifiedAdvertising;
import com.graduate.thesis.backend.repository.ClassifiedAdvertisingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * @author cuongbphv created on 15/05/2019
 */
@Service
public class ClassifiedAdvertisingServiceImpl implements ClassifiedAdvertisingService {

    @Autowired
    ClassifiedAdvertisingRepository classifiedAdvertisingRepository;

    @Override
    public ClassifiedAdvertising save(ClassifiedAdvertising classifiedAdvertising) {
        return classifiedAdvertisingRepository.save(classifiedAdvertising);
    }

    @Override
    public ClassifiedAdvertising getClassifiedAdsDetail(String id, int status) {
        return classifiedAdvertisingRepository.findClassifiedAdvertisingByIdAndStatus(id, status);
    }

    @Override
    public Page<ClassifiedAdvertising> getPagingNewAds(int pageNumber, int pageSize, int status) {
        return classifiedAdvertisingRepository.getNewAdsPaging(status,
                new PageRequest(pageNumber - 1, pageSize, Sort.Direction.DESC, "createdDate"));
    }
}
