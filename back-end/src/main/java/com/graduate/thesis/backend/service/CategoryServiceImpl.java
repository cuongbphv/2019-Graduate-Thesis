package com.graduate.thesis.backend.service;

import com.auth0.json.mgmt.Page;
import com.graduate.thesis.backend.entity.Category;
import com.graduate.thesis.backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author cuongbphv created on 16/04/2019
 */

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public void saveCategory(Category category) {
        categoryRepository.save(category);
    }

//    @Override
//    public Page<Category> getListCategory(
//            String searchKey, int pageNumber, int pageSize, boolean ascSort, int sortType
//    ) {
//        return categoryRepository.findAll();
//    }
}
