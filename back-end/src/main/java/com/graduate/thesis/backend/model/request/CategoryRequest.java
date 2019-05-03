package com.graduate.thesis.backend.model.request;

import com.graduate.thesis.backend.entity.model.Metadata;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Date;
import java.util.List;

/**
 * @author Huy Pham
 */
@Getter
@Setter
public class CategoryRequest {

    private String name;

    @Indexed
    private String slug;

    private String description;

    private String image;

    private String parentId;

    private List<Metadata> metadata;

    private List<Metadata> filter;

    private List<String> postType; //sell, buy, rent,...

}
