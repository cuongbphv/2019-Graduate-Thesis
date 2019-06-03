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
public class NewWardRequest {
    private String provinceId;
    private String districtId;
    private String type;
    private String slug;
    private String name;
    private List<WardRequest> wards;
}
