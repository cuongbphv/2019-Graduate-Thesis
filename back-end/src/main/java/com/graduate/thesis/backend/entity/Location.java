package com.graduate.thesis.backend.entity;

import com.graduate.thesis.backend.entity.model.District;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author cuongbphv created on 25/04/2019
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document("location")
public class Location {

    @Id
    private String id;

    private String name;

    private String slug;

    private String type;

    private String nameWithType;

    private String code;

    private List<District> districts;

}
