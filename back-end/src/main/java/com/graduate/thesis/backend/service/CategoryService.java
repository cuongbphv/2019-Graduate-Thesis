package com.graduate.thesis.backend.service;

import com.graduate.thesis.backend.entity.Category;
import com.graduate.thesis.backend.entity.Location;
import com.graduate.thesis.backend.model.response.CategoryResponse;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/**
 * @author cuongbphv created on 16/04/2019
 */
public interface CategoryService {

    Category save(Category category);

    Optional<Category> findBySlugAndStatus(String slug, int status);

    Optional<Category> findByNameAndStatusAndParentId(String name, int status, ObjectId parentId);

    Category findCategoryByIdAndStatus(String id, int status);

    List<CategoryResponse> getAllCategory(String searchKey, ObjectId parentObjId);

    Page<CategoryResponse> getPagingCategory(String searchKey, String parentId, int sortKey, boolean ascSort,
                                             int pageSize, int pageNumber);
}
