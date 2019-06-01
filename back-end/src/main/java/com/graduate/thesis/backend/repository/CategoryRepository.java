package com.graduate.thesis.backend.repository;

import com.graduate.thesis.backend.entity.Category;
import com.graduate.thesis.backend.entity.Location;
import com.graduate.thesis.backend.entity.Permission;
import com.graduate.thesis.backend.model.response.CategoryResponse;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author cuongbphv created on 16/04/2019
 */

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {

    Optional<Category> findBySlugAndStatus(String slug, int status);

    Optional<Category> findByNameAndStatusAndParentId(String name, int status, ObjectId parentId);

    @Query("{ '_id': ?0, 'status': ?1 }")
    Category findCategoryByIdAndStatus(String id, int status);

    @Query("{ 'name': {$regex : ?0, $options: 'i'}, 'parentId': ?1, status: ?2 }")
    Page<CategoryResponse> findCategoryLikeNameAndParentIdAndStatus(
            String name, ObjectId parentId, int status , Pageable pageable);

    @Query("{ 'name': {$regex : ?0, $options: 'i'}, 'parentId': ?1, status: ?2 }")
    List<CategoryResponse> findCategoryLikeNameAndParentIdAndStatus(
            String name, ObjectId parentId, int status);
}
