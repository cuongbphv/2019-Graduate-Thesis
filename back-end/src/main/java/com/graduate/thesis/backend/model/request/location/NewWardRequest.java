package com.graduate.thesis.backend.model.request.location;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author cuongbphv created on 05/05/2019
 */
@Getter
@Setter
public class NewWardRequest {
    private String provinceId;
    private String districtId;
    private List<WardRequest> wards;
}
