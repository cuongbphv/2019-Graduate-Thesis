package com.graduate.thesis.backend.repository;

import com.graduate.thesis.backend.entity.ClassifiedAdvertising;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author cuongbphv created on 15/05/2019
 */
@Repository
public interface ClassifiedAdvertisingRepository extends MongoRepository<ClassifiedAdvertising, String> {

    @Query("{ '_id': ?0, 'status': { $in: ?1 } }")
    ClassifiedAdvertising findClassifiedAdvertisingByIdAndStatus(String id, List<Integer> status);

    @Query("{ '_id': ?0, 'authorId': ?1 }")
    ClassifiedAdvertising findClassifiedAdvertisingByIdAndAuthorId(String id, String authorId);

    @Query("{ 'additionalInfo.title': {$regex : ?0, $options: 'i'}, 'status': { $in: ?1 } }")
    Page<ClassifiedAdvertising> getNewAdsPaging(String searchKey, List<Integer> status, Pageable pageable);

    @Query("{ 'additionalInfo.title': {$regex : ?0, $options: 'i'}, 'breadcrumbs.0': ?1, 'status': { $in: ?2 } }")
    Page<ClassifiedAdvertising> getNewAdsPagingHasCategory(String searchKey, String categoryId, List<Integer> status, Pageable pageable);

    @Query("{ 'additionalInfo.title': {$regex : ?0, $options: 'i'}, 'provinceId': ?1, 'breadcrumbs.0': ?2, 'status': { $in: ?3 } }")
    Page<ClassifiedAdvertising> getNewAdsPagingHasCategory(String searchKey, String provinceId, String categoryId, List<Integer> status, Pageable pageable);

    @Query("{ 'additionalInfo.title': {$regex : ?0, $options: 'i'}, 'provinceId': ?1, 'districtId': ?2, 'breadcrumbs.0': ?3, 'status': { $in: ?4 } }")
    Page<ClassifiedAdvertising> getNewAdsPagingHasCategory(String searchKey, String provinceId, String districtId, String categoryId, List<Integer> status, Pageable pageable);

    @Query("{ 'additionalInfo.title': {$regex : ?0, $options: 'i'}, 'provinceId': ?1, 'districtId': ?2, 'wardId': ?3, 'breadcrumbs.0': ?4, 'status': { $in: ?5 } }")
    Page<ClassifiedAdvertising> getNewAdsPagingHasCategory(String searchKey, String provinceId, String districtId, String wardId, String categoryId, List<Integer> status, Pageable pageable);

    @Query("{ 'additionalInfo.title': {$regex : ?0, $options: 'i'}, 'provinceId': ?1, 'status': { $in: ?2 } }")
    Page<ClassifiedAdvertising> getNewAdsPagingNoCategory(String searchKey, String provinceId, List<Integer> status, Pageable pageable);

    @Query("{ 'additionalInfo.title': {$regex : ?0, $options: 'i'}, 'provinceId': ?1, 'districtId': ?2, 'status': { $in: ?3 } }")
    Page<ClassifiedAdvertising> getNewAdsPagingNoCategory(String searchKey, String provinceId, String districtId, List<Integer> status, Pageable pageable);

    @Query("{ 'additionalInfo.title': {$regex : ?0, $options: 'i'}, 'provinceId': ?1, 'districtId': ?2, 'wardId': ?3, 'status': { $in: ?4 } }")
    Page<ClassifiedAdvertising> getNewAdsPagingNoCategory(String searchKey, String provinceId, String districtId, String wardId, List<Integer> status, Pageable pageable);

    // CASE QUERY BY USER ID

    @Query("{ 'additionalInfo.title': {$regex : ?0, $options: 'i'}, 'breadcrumbs.0': ?1, 'status': { $in: ?2 }, 'authorId': ?3 }")
    Page<ClassifiedAdvertising> getUserHistoryAdsPagingHasCategory(String searchKey, String categoryId, List<Integer> status, String authorId, Pageable pageable);

    @Query("{ 'additionalInfo.title': {$regex : ?0, $options: 'i'}, 'provinceId': ?1, 'breadcrumbs.0': ?2, 'status': { $in: ?3 }, 'authorId': ?4 }")
    Page<ClassifiedAdvertising> getUserHistoryAdsPagingHasCategory(String searchKey, String provinceId, String categoryId, List<Integer> status, String authorId, Pageable pageable);

    @Query("{ 'additionalInfo.title': {$regex : ?0, $options: 'i'}, 'provinceId': ?1, 'districtId': ?2, 'breadcrumbs.0': ?3, 'status': { $in: ?4 }, 'authorId': ?5 }")
    Page<ClassifiedAdvertising> getUserHistoryAdsPagingHasCategory(String searchKey, String provinceId, String districtId, String categoryId, List<Integer> status, String authorId, Pageable pageable);

    @Query("{ 'additionalInfo.title': {$regex : ?0, $options: 'i'}, 'provinceId': ?1, 'districtId': ?2, 'wardId': ?3, 'breadcrumbs.0': ?4, 'status': { $in: ?5 }, 'authorId': ?6 }")
    Page<ClassifiedAdvertising> getUserHistoryAdsPagingHasCategory(String searchKey, String provinceId, String districtId, String wardId, String categoryId, List<Integer> status, String authorId, Pageable pageable);

    @Query("{ 'additionalInfo.title': {$regex : ?0, $options: 'i'}, 'status': { $in: ?1 }, 'authorId': ?2 }")
    Page<ClassifiedAdvertising> getUserHistoryAdsPagingNoCategory(String searchKey, List<Integer> status, String authorId, Pageable pageable);

    @Query("{ 'additionalInfo.title': {$regex : ?0, $options: 'i'}, 'provinceId': ?1, 'status': { $in: ?2 }, 'authorId': ?3 }")
    Page<ClassifiedAdvertising> getUserHistoryAdsPagingNoCategory(String searchKey, String provinceId, List<Integer> status, String authorId, Pageable pageable);

    @Query("{ 'additionalInfo.title': {$regex : ?0, $options: 'i'}, 'provinceId': ?1, 'districtId': ?2, 'status': { $in: ?3 }, 'authorId': ?4 }")
    Page<ClassifiedAdvertising> getUserHistoryAdsPagingNoCategory(String searchKey, String provinceId, String districtId, List<Integer> status, String authorId, Pageable pageable);

    @Query("{ 'additionalInfo.title': {$regex : ?0, $options: 'i'}, 'provinceId': ?1, 'districtId': ?2, 'wardId': ?3, 'status': { $in: ?4 }, 'authorId': ?5 }")
    Page<ClassifiedAdvertising> getUserHistoryAdsPagingNoCategory(String searchKey, String provinceId, String districtId, String wardId, List<Integer> status, String authorId, Pageable pageable);

}
