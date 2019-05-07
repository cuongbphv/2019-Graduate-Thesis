package com.graduate.thesis.backend.controller;

import com.graduate.thesis.backend.entity.Category;
import com.graduate.thesis.backend.entity.model.Metadata;
import com.graduate.thesis.backend.exception.ApplicationException;
import com.graduate.thesis.backend.model.request.CategoryRequest;
import com.graduate.thesis.backend.model.response.CategoryResponse;
import com.graduate.thesis.backend.model.response.RestAPIResponse;
import com.graduate.thesis.backend.repository.aggregation.CategoryAggregation;
import com.graduate.thesis.backend.service.CategoryService;
import com.graduate.thesis.backend.service.FileStorageService;
import com.graduate.thesis.backend.util.APIStatus;
import com.graduate.thesis.backend.util.CommonUtil;
import com.graduate.thesis.backend.util.Constant;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Date;
import java.util.List;

/**
 * @author cuongbphv created on 16/04/2019
 */

@RestController
@RequestMapping(Constant.CATEGORY_API)
public class CategoryController extends AbstractBasedAPI {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryAggregation categoryAggregation;

    @Autowired
    FileStorageService fileUploadService;

    @RequestMapping(value = Constant.WITHIN_ID, method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> getCategory(
            @PathVariable("id") String categoryId
    ){

        CategoryResponse category = categoryAggregation.getCategoryById(categoryId);

        if(category == null){
            throw new ApplicationException(APIStatus.ERR_CATEGORY_NOT_FOUND);
        }

        return responseUtil.successResponse(category);
    }

//    @RequestMapping(method = RequestMethod.GET)
//    public ResponseEntity<RestAPIResponse> getParentCategory(
//    ){
//
//        List<CategoryResponse> categories = categoryAggregation.getParentCategory();
//
//        if(categories == null){
//            throw new ApplicationException(APIStatus.ERR_CATEGORY_NOT_FOUND);
//        }
//
//        return responseUtil.successResponse(categories);
//    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> getListPagingLocation (
            @RequestParam(value = "search_key", defaultValue = "") String searchKey,
            @RequestParam( value = "parent_id", defaultValue = "") String parentId,
            @RequestParam("sort_key") int sortKey,
            @RequestParam("asc_sort") boolean ascSort,
            @RequestParam("page_size") int pageSize,
            @RequestParam("page_number") int pageNumber
    ) {

        Page<CategoryResponse> locations = categoryService.getPagingCategory(searchKey,parentId,
                sortKey, ascSort, pageSize, pageNumber);

        return responseUtil.successResponse(locations);
    }


    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<RestAPIResponse> createCategory(
            @RequestBody CategoryRequest categoryRequest
            ){

        if(categoryService.findBySlugAndStatus(categoryRequest.getSlug(),
                Constant.Status.ACTIVE.getValue()).isPresent()){
            throw new ApplicationException(APIStatus.ERR_CATEGORY_EXISTED);
        }

        ObjectId parentId;

        if( categoryRequest.getParentId() == null || categoryRequest.getParentId().isEmpty()){
            parentId = null;
        }
        else{
            parentId = new ObjectId(categoryRequest.getParentId());
        }

        Category category = new Category();
        category.setParentId(parentId);
        category.setName(categoryRequest.getName());
        category.setSlug(categoryRequest.getSlug());
        category.setDescription(categoryRequest.getDescription());
        category.setImage(categoryRequest.getImage());
        category.setMetadata(categoryRequest.getMetadata());
        category.setFilter(categoryRequest.getMetadata());
        category.setPostType(categoryRequest.getPostType());

        category.setCreatedDate(new Date());
        category.setStatus(Constant.Status.ACTIVE.getValue());

        Category created = categoryService.save(category);

        return responseUtil.successResponse(created);
    }


    @RequestMapping(value = Constant.WITHIN_ID, method = RequestMethod.PUT)
    public ResponseEntity<RestAPIResponse> updateCategory(
            @RequestBody CategoryRequest categoryRequest,
            @PathVariable("id") String categoryId
    ){

        Category existedCategory = categoryService.findCategoryByIdAndStatus(categoryId,
                Constant.Status.ACTIVE.getValue());

        if(existedCategory == null){
            throw new ApplicationException(APIStatus.ERR_CATEGORY_NOT_FOUND);
        }

        ObjectId parentId;

        if(categoryRequest.getParentId() == null || categoryRequest.getParentId().isEmpty()){
            parentId = null;
        }
        else{
            parentId = new ObjectId(categoryRequest.getParentId());
        }

        existedCategory.setParentId(parentId);
        existedCategory.setName(categoryRequest.getName());
        existedCategory.setSlug(categoryRequest.getSlug());
        existedCategory.setDescription(categoryRequest.getDescription());
        existedCategory.setImage(categoryRequest.getImage());
        existedCategory.setMetadata(categoryRequest.getMetadata());
        existedCategory.setFilter(categoryRequest.getFilter());
        existedCategory.setPostType(categoryRequest.getPostType());

        Category updated = categoryService.save(existedCategory);

        return responseUtil.successResponse(updated);
    }


    @RequestMapping(value = Constant.WITHIN_ID + Constant.METADATA, method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> getMetadata(
            @PathVariable("id") String categoryId
    ) {

        Category existedCategory = categoryService.findCategoryByIdAndStatus(categoryId,
                Constant.Status.ACTIVE.getValue());

        if(existedCategory == null){
            throw new ApplicationException(APIStatus.ERR_CATEGORY_NOT_FOUND);
        }

        return responseUtil.successResponse(existedCategory.getMetadata());
    }

    @RequestMapping(value = Constant.WITHIN_ID + Constant.FILTER, method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> getFilter(
            @PathVariable("id") String categoryId
    ) {

        Category existedCategory = categoryService.findCategoryByIdAndStatus(categoryId,
                Constant.Status.ACTIVE.getValue());

        if(existedCategory == null){
            throw new ApplicationException(APIStatus.ERR_CATEGORY_NOT_FOUND);
        }

        return responseUtil.successResponse(existedCategory.getFilter());
    }

    @RequestMapping(value = Constant.WITHIN_ID + Constant.METADATA, method = RequestMethod.PUT)
    public ResponseEntity<RestAPIResponse> updateMetadata(
            @RequestBody List<Metadata> metadata,
            @PathVariable("id") String categoryId
    ) {

        Category existedCategory = categoryService.findCategoryByIdAndStatus(categoryId,
                Constant.Status.ACTIVE.getValue());

        if(existedCategory == null){
            throw new ApplicationException(APIStatus.ERR_CATEGORY_NOT_FOUND);
        }

        existedCategory.setMetadata(metadata);

        Category updated = categoryService.save(existedCategory);

        return responseUtil.successResponse(updated.getMetadata());
    }

    @RequestMapping(value = Constant.WITHIN_ID + Constant.FILTER, method = RequestMethod.PUT)
    public ResponseEntity<RestAPIResponse> updateFilter(
            @RequestBody List<Metadata> filter,
            @PathVariable("id") String categoryId
    ) {

        Category existedCategory = categoryService.findCategoryByIdAndStatus(categoryId,
                Constant.Status.ACTIVE.getValue());

        if(existedCategory == null){
            throw new ApplicationException(APIStatus.ERR_CATEGORY_NOT_FOUND);
        }

        existedCategory.setFilter(filter);

        Category updated = categoryService.save(existedCategory);

        return responseUtil.successResponse(updated.getFilter());
    }

    @RequestMapping(value = Constant.IMAGE, method = RequestMethod.PUT)
    public ResponseEntity<RestAPIResponse> updateUserAvatar(
            @RequestPart(value = "avatar") MultipartFile avatarImg
    ) {

        if (avatarImg == null) {
            throw new ApplicationException(APIStatus.ERR_BAD_REQUEST);
        }

        String fileName = "category_" + CommonUtil.generateUUID() + CommonUtil.getFileExtension(avatarImg);

        String url = fileUploadService.storeFile(avatarImg, fileName);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/files/")
                .path(fileName)
                .toUriString();

        return responseUtil.successResponse(fileDownloadUri);
    }
}
