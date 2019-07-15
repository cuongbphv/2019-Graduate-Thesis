package com.graduate.thesis.backend.service;

import com.graduate.thesis.backend.entity.ClassifiedAdvertising;
import com.graduate.thesis.backend.entity.UserProfile;
import com.graduate.thesis.backend.entity.elastic.ClassifiedAdvertisingElastic;
import com.graduate.thesis.backend.model.response.AddressResponse;
import com.graduate.thesis.backend.repository.ClassifiedAdvertisingRepository;
import com.graduate.thesis.backend.repository.aggregation.AddressAggregation;
import com.graduate.thesis.backend.service.elastic.ClassifiedAdvertisingElasticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cuongbphv created on 15/05/2019
 */
@Service
public class ClassifiedAdvertisingServiceImpl implements ClassifiedAdvertisingService {

    @Autowired
    ClassifiedAdvertisingRepository classifiedAdvertisingRepository;

    @Autowired
    ClassifiedAdvertisingElasticService classifiedAdvertisingElasticService;

    @Autowired
    AddressAggregation addressAggregation;

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

        List<ClassifiedAdvertising> content = new ArrayList<>();

        List<ClassifiedAdvertising> listAds;

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

        Sort sort = new Sort(ascSort ? Sort.Direction.ASC : Sort.Direction.DESC, properties);

        if (categoryId.isEmpty()) {
            listAds = classifiedAdvertisingRepository.getNewAdsPaging(searchKey, status, sort);
        } else {
            listAds = classifiedAdvertisingRepository.getNewAdsPagingHasCategory(searchKey, categoryId, status, sort);
        }

        for (ClassifiedAdvertising ads : listAds) {
            // classified ads of user has detail address
            if (ads.getAddressId() != null) {
                // get address detail
                AddressResponse addressResponse = addressAggregation.getAddressByAddressIdAndUserId(
                        ads.getAddressId(),
                        ads.getAuthorId()
                );
                // if can not find address break this ads
                if (addressResponse == null) continue;
                // check valid case
                if (!wardId.isEmpty()) {
                    if (addressResponse.getWard().getId().equals(wardId)) {
                        content.add(ads);
                    }
                    continue;
                }
                if (!districtId.isEmpty()) {
                    if (addressResponse.getDistrict().getId().equals(districtId)) {
                        content.add(ads);
                    }
                    continue;
                }
                if (!provinceId.isEmpty()) {
                    if (addressResponse.getProvince().getId().equals(provinceId)) {
                        content.add(ads);
                    }
                    continue;
                }
            }

            if (!wardId.isEmpty()) {
                if (ads.getWardId().equals(wardId)) {
                    content.add(ads);
                }
                continue;
            }
            if (!districtId.isEmpty()) {
                if (ads.getDistrictId().equals(districtId)) {
                    content.add(ads);
                }
                continue;
            }
            if (!provinceId.isEmpty()) {
                if (ads.getProvinceId().equals(provinceId)) {
                    content.add(ads);
                }
                continue;
            }
            // all above case is wrong
            content.add(ads);
        }

        Pageable pageable = new PageRequest(pageNumber - 1, pageSize);
        int start = pageable.getPageNumber() * pageable.getPageSize();
        int end = (start + pageable.getPageSize()) > content.size() ? content.size() : (start + pageable.getPageSize());

        return new PageImpl<>(content.subList(start, end), pageable, content.size());
    }

    @Override
    public Page<ClassifiedAdvertising> getPagingAdsByAuthorId(
            String authorId,
            String searchKey,
            String categoryId,
            boolean ascSort,
            int pageNumber,
            int pageSize,
            List<Integer> status
    ) {

        Pageable pageable = new PageRequest(pageNumber - 1, pageSize,
                ascSort ? Sort.Direction.ASC : Sort.Direction.DESC, "createdDate");

        if (categoryId.isEmpty()) {
            return classifiedAdvertisingRepository.getUserHistoryAdsPagingNoCategory(
                    searchKey, status, authorId, pageable);
        } else {
            return classifiedAdvertisingRepository.getUserHistoryAdsPagingHasCategory(
                    searchKey, categoryId, status, authorId, pageable);
        }
    }

    @Override
    @Async
    public void reIndexProfile(UserProfile userProfile) {

        List<ClassifiedAdvertisingElastic> advertisingElastics =
                classifiedAdvertisingElasticService.getByAuthorId(userProfile.getUserId());

        if(advertisingElastics != null){

            for( ClassifiedAdvertisingElastic advertisingElastic: advertisingElastics ){
                advertisingElastic.setAuthor(userProfile);
                classifiedAdvertisingElasticService.index(advertisingElastic);
            }

        }
    }

    @Override
    public long countAllPost() {
        return classifiedAdvertisingRepository.count();
    }
}
