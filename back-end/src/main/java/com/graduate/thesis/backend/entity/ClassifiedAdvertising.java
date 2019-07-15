package com.graduate.thesis.backend.entity;

import com.graduate.thesis.backend.entity.elastic.ClassifiedAdvertisingElastic;
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

    private List<String> saves;

    private Date createdDate;

    private Date modifiedDate;

    private int tradeStatus; // 0: pending, 1: available, 2: sell, 3: hide

    private int status;

    public enum TradeStatus {
        PENDING(0),
        AVAILABLE(1),
        SOLD(2),
        HIDE(3),
        ;

        private int value;

        TradeStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public ClassifiedAdvertising(ClassifiedAdvertisingElastic classifiedAdvertisingElastic){

        this.id = classifiedAdvertisingElastic.getId();
        this.authorId = classifiedAdvertisingElastic.getAuthor().getId();
        this.locationType = classifiedAdvertisingElastic.getLocationType();
        this.images = classifiedAdvertisingElastic.getImages();
        this.additionalInfo = classifiedAdvertisingElastic.getAdditionalInfo();
        this.breadcrumbs = classifiedAdvertisingElastic.getBreadcrumbs();
        this.metadata = classifiedAdvertisingElastic.getMetadata();
        this.createdDate = classifiedAdvertisingElastic.getCreatedDate();
        this.modifiedDate = classifiedAdvertisingElastic.getModifiedDate();
        this.status = classifiedAdvertisingElastic.getStatus();

        if(this.locationType == 1){
            this.wardId = classifiedAdvertisingElastic.getAddress().getWard().getId();
            this.districtId = classifiedAdvertisingElastic.getAddress().getDistrict().getId();
            this.provinceId = classifiedAdvertisingElastic.getAddress().getProvince().getId();
        }
        else{
            this.addressId = classifiedAdvertisingElastic.getAddress().getId();
        }

    }
}
