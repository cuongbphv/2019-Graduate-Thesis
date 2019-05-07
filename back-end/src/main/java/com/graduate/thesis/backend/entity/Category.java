package com.graduate.thesis.backend.entity;

import com.graduate.thesis.backend.entity.model.Metadata;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * @author Huy Pham
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

    @Indexed
    private String slug;

    private String description;

    private String image;

    private ObjectId parentId;

    private List<Metadata> metadata;

    private List<Metadata> filter;

    private List<String> postType; //sell, buy, rent,...

    private Date createdDate;

    private int status;
}
