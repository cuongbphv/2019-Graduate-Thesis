package com.graduate.thesis.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author cuongbphv created on 16/04/2019
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document("category")
public class Category {

    @Id
    private String id;

    private String name;

    private String description;

    private String parentId;

    private Date createdDate;

    private String metadata;

    private int status;
}
