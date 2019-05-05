package com.graduate.thesis.backend.model.request.location;

import lombok.Getter;
import lombok.Setter;

/**
 * @author cuongbphv created on 05/05/2019
 */
@Getter
@Setter
public class WardRequest {
    private String id;
    private String name;
    private String type;
    private String slug;
    private String nameWithType;
}
