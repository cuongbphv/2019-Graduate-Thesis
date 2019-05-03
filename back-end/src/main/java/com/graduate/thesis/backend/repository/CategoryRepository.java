package com.graduate.thesis.backend.repository;

import com.graduate.thesis.backend.entity.Category;
import com.graduate.thesis.backend.entity.Permission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author cuongbphv created on 16/04/2019
 */

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {

    Optional<Category> findBySlugAndStatus(String name, int status);

    @Query("{ '_id': ?0, 'status': ?1 }")
    Category findCategoryByIdAndStatus(String id, int status);

}
