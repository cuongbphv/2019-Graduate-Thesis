package com.graduate.thesis.backend.model.request.advertising;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

/**
 * @author cuongbphv created on 12/05/2019
 */
@Getter
@Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AdditionalInfo {
    private String title;
    private String description;
    private String adsType;
    private long maxPrice;
    private long price;
    private int[] rangePrice;
}
