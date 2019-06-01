package com.graduate.thesis.backend.service;

import com.graduate.thesis.backend.entity.ClassifiedAdvertising;
import com.graduate.thesis.backend.repository.ClassifiedAdvertisingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public ClassifiedAdvertising getClassifiedAdsDetail(String id, List<Integer> status) {
        return classifiedAdvertisingRepository.findClassifiedAdvertisingByIdAndStatus(id, status);
    }

    @Override
    public Page<ClassifiedAdvertising> getPagingNewAds(String searchKey, int sortKey, boolean ascSort,
                                                       int pageNumber, int pageSize, List<Integer> status) {

        if (searchKey.isEmpty()) {
            searchKey = " ";
        }

        String properties = "";

        switch (sortKey) {
            case 1:
                properties = "_id"; break;
            case 2:
                properties = "name"; break;
            case 3:
                properties = "type"; break;
            case 4:
                properties = "createdDate"; break;
            default:
                properties = "createdDate";
        }

        return classifiedAdvertisingRepository.getNewAdsPaging(searchKey, status,
                new PageRequest(pageNumber - 1, pageSize,
                        ascSort ? Sort.Direction.ASC : Sort.Direction.DESC, properties));
    }
}
