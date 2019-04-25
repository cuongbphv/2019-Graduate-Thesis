package com.graduate.thesis.backend.entity.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author cuongbphv created on 25/04/2019
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ward {

    private String id;

    private String name;

    private String type;

    private String slug;

    private String nameWithType;

    private String path;

    private String pathWithType;

    private String code;

    private String parentCode;

}
