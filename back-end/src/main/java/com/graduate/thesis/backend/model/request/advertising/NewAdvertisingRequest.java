package com.graduate.thesis.backend.model.request.advertising;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.graduate.thesis.backend.entity.Location;
import com.graduate.thesis.backend.entity.model.District;
import com.graduate.thesis.backend.entity.model.Ward;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author cuongbphv created on 12/05/2019
 */
@Getter
@Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class NewAdvertisingRequest {

    private int locationType;
    private String provinceId;
    private String districtId;
    private String wardId;
    private String addressId;

    private List<Image> images;

    private AdditionalInfo additionalInfo;

    private List<String> breadcrumbs;
    private List<AdsMetadata> metadata;
}
