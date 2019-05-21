package com.graduate.thesis.backend.entity;

import com.graduate.thesis.backend.model.request.advertising.AdditionalInfo;
import com.graduate.thesis.backend.model.request.advertising.AdsMetadata;
import com.graduate.thesis.backend.model.request.advertising.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * @author cuongbphv created on 15/05/2019
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document("classified_advertising")
public class ClassifiedAdvertising {

    @Id
    private String id;

    private String authorId;

    private int locationType;

    private String provinceId;

    private String districtId;

    private String wardId;

    private String addressId;

    private List<Image> images;

    private AdditionalInfo additionalInfo;

    private List<String> breadcrumbs;

    private List<AdsMetadata> metadata;

    @CreatedDate
    private Date createdDate;

    private int status;
}
