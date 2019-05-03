package com.graduate.thesis.backend.service;

import com.graduate.thesis.backend.entity.Category;

import java.util.Optional;

/**
 * @author cuongbphv created on 16/04/2019
 */
public interface CategoryService {

    Category save(Category category);

    Optional<Category> findBySlugAndStatus(String slug, int status);

    Category findCategoryByIdAndStatus(String id, int status);
}
