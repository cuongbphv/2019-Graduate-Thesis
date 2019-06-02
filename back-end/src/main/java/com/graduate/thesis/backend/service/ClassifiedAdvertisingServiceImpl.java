package com.graduate.thesis.backend.service;

import com.graduate.thesis.backend.entity.ClassifiedAdvertising;
import com.graduate.thesis.backend.repository.ClassifiedAdvertisingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
    public ClassifiedAdvertising getClassifiedAdsDetailByIdAndAuthor(String id, String authorId) {
        return classifiedAdvertisingRepository.findClassifiedAdvertisingByIdAndAuthorId(id, authorId);
    }

    @Override
    public Page<ClassifiedAdvertising> getPagingNewAds(
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
    ) {

        String properties = "";

        switch (sortKey) {
            case 1:
                properties = "additionalInfo.title"; break;
            case 2:
                properties = "createdDate"; break;
            case 3:
                properties = "status"; break;
            default:
                properties = "createdDate";
        }

        Pageable pageable = new PageRequest(pageNumber - 1, pageSize,
                ascSort ? Sort.Direction.ASC : Sort.Direction.DESC, properties);

        if (categoryId.isEmpty()) {
            if (!wardId.isEmpty()) {
                return classifiedAdvertisingRepository.getNewAdsPagingNoCategory(
                        searchKey, provinceId, districtId, wardId, status, pageable);
            }
            if (!districtId.isEmpty()) {
                return classifiedAdvertisingRepository.getNewAdsPagingNoCategory(
                        searchKey, provinceId, districtId, status, pageable);
            }
            if (!provinceId.isEmpty()) {
                return classifiedAdvertisingRepository.getNewAdsPagingNoCategory(
                        searchKey, provinceId, status, pageable);
            }
            return classifiedAdvertisingRepository.getNewAdsPaging(searchKey, status, pageable);
        } else {
            if (!wardId.isEmpty()) {
                return classifiedAdvertisingRepository.getNewAdsPagingHasCategory(
                        searchKey, provinceId, districtId, wardId, categoryId, status, pageable);
            }
            if (!districtId.isEmpty()) {
                return classifiedAdvertisingRepository.getNewAdsPagingHasCategory(
                        searchKey, provinceId, districtId, categoryId, status, pageable);
            }
            if (!provinceId.isEmpty()) {
                return classifiedAdvertisingRepository.getNewAdsPagingHasCategory(
                        searchKey, provinceId, categoryId, status, pageable);
            }
            return classifiedAdvertisingRepository.getNewAdsPagingHasCategory(
                    searchKey, categoryId, status, pageable);
        }
    }

    @Override
    public Page<ClassifiedAdvertising> getPagingAdsByAuthorId(
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
    ) {

        Pageable pageable = new PageRequest(pageNumber - 1, pageSize,
                ascSort ? Sort.Direction.ASC : Sort.Direction.DESC, "createdDate");

        if (categoryId.isEmpty()) {
            if (!wardId.isEmpty()) {
                return classifiedAdvertisingRepository.getUserHistoryAdsPagingNoCategory(
                        searchKey, provinceId, districtId, wardId, status, authorId, pageable);
            }
            if (!districtId.isEmpty()) {
                return classifiedAdvertisingRepository.getUserHistoryAdsPagingNoCategory(
                        searchKey, provinceId, districtId, status, authorId, pageable);
            }
            if (!provinceId.isEmpty()) {
                return classifiedAdvertisingRepository.getUserHistoryAdsPagingNoCategory(
                        searchKey, provinceId, status, authorId, pageable);
            }
            return classifiedAdvertisingRepository.getUserHistoryAdsPagingNoCategory(searchKey, status, authorId, pageable);
        } else {
            if (!wardId.isEmpty()) {
                return classifiedAdvertisingRepository.getUserHistoryAdsPagingHasCategory(
                        searchKey, provinceId, districtId, wardId, categoryId, status, authorId, pageable);
            }
            if (!districtId.isEmpty()) {
                return classifiedAdvertisingRepository.getUserHistoryAdsPagingHasCategory(
                        searchKey, provinceId, districtId, categoryId, status, authorId, pageable);
            }
            if (!provinceId.isEmpty()) {
                return classifiedAdvertisingRepository.getUserHistoryAdsPagingHasCategory(
                        searchKey, provinceId, categoryId, status, authorId, pageable);
            }
            return classifiedAdvertisingRepository.getUserHistoryAdsPagingHasCategory(
                    searchKey, categoryId, status, authorId, pageable);
        }
    }
}
