package com.graduate.thesis.backend.repository;

import com.graduate.thesis.backend.entity.ClassifiedAdvertising;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    List<ClassifiedAdvertising> getNewAdsPaging(String searchKey, List<Integer> status, Sort sort);

    @Query("{ 'additionalInfo.title': {$regex : ?0, $options: 'i'}, 'breadcrumbs.0': ?1, 'status': { $in: ?2 } }")
    List<ClassifiedAdvertising> getNewAdsPagingHasCategory(String searchKey, String categoryId, List<Integer> status, Sort sort);

    @Query("{ 'additionalInfo.title': {$regex : ?0, $options: 'i'}, 'breadcrumbs.0': ?1, 'status': { $in: ?2 }, 'authorId': ?3 }")
    Page<ClassifiedAdvertising> getUserHistoryAdsPagingHasCategory(String searchKey, String categoryId, List<Integer> status, String authorId, Pageable pageable);

    @Query("{ 'additionalInfo.title': {$regex : ?0, $options: 'i'}, 'status': { $in: ?1 }, 'authorId': ?2 }")
    Page<ClassifiedAdvertising> getUserHistoryAdsPagingNoCategory(String searchKey, List<Integer> status, String authorId, Pageable pageable);

}
