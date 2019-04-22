package com.graduate.thesis.backend.controller;

import com.graduate.thesis.backend.util.Constant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cuongbphv created on 16/04/2019
 */

@RestController
@RequestMapping(Constant.CATEGORY_API)
public class CategoryController extends AbstractBasedAPI {

    @GetMapping()
    public ResponseEntity<?> getListCategories() {
        return responseUtil.successResponse("");
    }

    @PostMapping()
    public ResponseEntity<?> createNewCategory() {
        return responseUtil.successResponse("");
    }
}
