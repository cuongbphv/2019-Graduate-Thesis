package com.graduate.thesis.backend.model.response;

import com.graduate.thesis.backend.entity.Category;
import com.graduate.thesis.backend.entity.model.Metadata;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @author Huy Pham
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {

    private String id;

    private String name;

    private String slug;

    private String description;

    private String image;

    private String parentId;

    private List<Metadata> metadata;

    private List<Metadata> filter;

    private List<String> postType;

    private Date createdDate;

    private int status;

    private List<Category> subs;
}
