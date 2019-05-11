package com.graduate.thesis.backend.service;

import com.graduate.thesis.backend.entity.Category;
import com.graduate.thesis.backend.entity.Location;
import com.graduate.thesis.backend.model.response.CategoryResponse;
import com.graduate.thesis.backend.repository.CategoryRepository;
import com.graduate.thesis.backend.util.Constant;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Huy Pham
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
    public Optional<Category> findByNameAndStatusAndParentId(String name, int status, ObjectId parentId) {
        return categoryRepository.findByNameAndStatusAndParentId(name, status, parentId);
    }

    @Override
    public Category findCategoryByIdAndStatus(String id, int status) {
        return categoryRepository.findCategoryByIdAndStatus(id,status);
    }

    @Override
    public Page<CategoryResponse> getPagingCategory(String searchKey, String parentId, int sortKey, boolean ascSort, int pageSize, int pageNumber) {

        String properties = "createdDate";

        if(sortKey == 1){
            properties = "name";
        }


        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize,
                ascSort ? Sort.Direction.ASC : Sort.Direction.DESC, properties);

        ObjectId parent;

        if(parentId.isEmpty() || parentId == null){
            parent = null;
        }
        else{
            parent = new ObjectId(parentId);
        }

        return categoryRepository.findCategoryLikeNameAndParentIdAndStatus(
                searchKey, parent, Constant.Status.ACTIVE.getValue(), pageRequest);
    }

//    @Override
//    public Page<Category> getListCategory(
//            String searchKey, int pageNumber, int pageSize, boolean ascSort, int sortType
//    ) {
//        return categoryRepository.findAll();
//    }
}
