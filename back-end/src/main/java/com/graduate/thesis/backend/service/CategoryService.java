package com.graduate.thesis.backend.service;

import com.auth0.json.mgmt.Page;
import com.graduate.thesis.backend.entity.Category;

/**
 * @author cuongbphv created on 16/04/2019
 */
public interface CategoryService {
    void saveCategory(Category category);

//    Page<Category> getListCategory(String searchKey, int pageNumber, int pageSize, boolean ascSort, int sortType);
}
