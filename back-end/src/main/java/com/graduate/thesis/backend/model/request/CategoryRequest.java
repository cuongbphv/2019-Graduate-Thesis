package com.graduate.thesis.backend.model.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author cuongbphv created on 19/04/2019
 */
@Getter
@Setter
public class CategoryRequest {

    private String id;

    private String name;

    private String description;

    private String parentId;

    private String metadata;
}
