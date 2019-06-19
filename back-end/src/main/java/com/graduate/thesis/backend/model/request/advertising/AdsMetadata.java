package com.graduate.thesis.backend.model.request.advertising;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author cuongbphv created on 15/05/2019
 */
@Getter
@Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AdsMetadata {

    private String label;

    private String enLabel;

    private String slug;

    private String selectionType;

    private String type;

    private String value;

    private String valueLabel;

    private String enValueLabel;

}
