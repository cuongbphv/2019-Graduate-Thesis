package com.graduate.thesis.backend.service;

import com.auth0.json.mgmt.Page;
import com.graduate.thesis.backend.entity.Category;
import com.graduate.thesis.backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author cuongbphv created on 16/04/2019
 */

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Optional<Category> findBySlugAndStatus(String slug, int status) {
        return categoryRepository.findBySlugAndStatus(slug, status);
    }

    @Override
    public Category findCategoryByIdAndStatus(String id, int status) {
        return categoryRepository.findCategoryByIdAndStatus(id,status);
    }

//    @Override
//    public Page<Category> getListCategory(
//            String searchKey, int pageNumber, int pageSize, boolean ascSort, int sortType
//    ) {
//        return categoryRepository.findAll();
//    }
}
