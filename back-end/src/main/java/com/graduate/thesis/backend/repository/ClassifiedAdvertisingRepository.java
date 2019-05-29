package com.graduate.thesis.backend.repository;

import com.graduate.thesis.backend.entity.ClassifiedAdvertising;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author cuongbphv created on 15/05/2019
 */
@Repository
public interface ClassifiedAdvertisingRepository extends MongoRepository<ClassifiedAdvertising, String> {

    @Query("{ '_id': ?0, 'status': ?1 }")
    ClassifiedAdvertising findClassifiedAdvertisingByIdAndStatus(String id, int status);

    @Query("{ 'status': ?0 }")
    Page<ClassifiedAdvertising> getNewAdsPaging(int status, Pageable pageable);

}
