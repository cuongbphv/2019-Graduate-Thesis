package com.graduate.thesis.backend.repository;

import com.graduate.thesis.backend.entity.ClassifiedAdvertising;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author cuongbphv created on 15/05/2019
 */
@Repository
public interface ClassifiedAdvertisingRepository extends MongoRepository<ClassifiedAdvertising, String> {
}