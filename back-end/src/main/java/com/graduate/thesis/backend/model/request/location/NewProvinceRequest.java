package com.graduate.thesis.backend.model.request.location;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author cuongbphv created on 05/05/2019
 */
@Getter
@Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class NewProvinceRequest {
    private String id;
    private String name;
    private String type;
    private String slug;
    private String nameWithType;
    private List<DistrictRequest> districts;
}
