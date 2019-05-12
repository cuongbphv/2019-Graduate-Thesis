package com.graduate.thesis.backend.model.request.advertising;

import lombok.Getter;
import lombok.Setter;

/**
 * @author cuongbphv created on 12/05/2019
 */
@Getter
@Setter
public class Image {
    private String name;
    private long uid;
    private int percent;
    private String url;
    private long size;
    private String status;
    private Object response;
}
