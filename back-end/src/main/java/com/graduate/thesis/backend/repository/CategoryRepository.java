package com.graduate.thesis.backend.repository;

import com.graduate.thesis.backend.entity.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author cuongbphv created on 16/04/2019
 */

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
}
