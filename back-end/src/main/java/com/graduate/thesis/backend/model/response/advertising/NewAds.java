package com.graduate.thesis.backend.model.response.advertising;

import com.graduate.thesis.backend.model.request.advertising.Image;
import com.graduate.thesis.backend.model.response.AddressResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @author cuongbphv created on 19/05/2019
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewAds {
    // author
    private String authorId;
    private String firstName;
    private String lastName;
    private String avatarUrl;
    // classified advertising
    private String classifiedAdsId;
    private String title;
    private Date createdDate;
    private AddressResponse addressResponse;
    private Image thumbnail;
    // category
    private String categoryId;
    private String categoryName;
}
