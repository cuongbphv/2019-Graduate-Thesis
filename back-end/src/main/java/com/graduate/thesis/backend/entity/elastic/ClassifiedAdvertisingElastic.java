package com.graduate.thesis.backend.entity.elastic;

import com.graduate.thesis.backend.entity.Address;
import com.graduate.thesis.backend.entity.UserProfile;
import com.graduate.thesis.backend.model.request.advertising.AdditionalInfo;
import com.graduate.thesis.backend.model.request.advertising.AdsMetadata;
import com.graduate.thesis.backend.model.request.advertising.Image;
import com.graduate.thesis.backend.model.response.AddressResponse;
import com.graduate.thesis.backend.security.oauth2.user.UserPrincipal;
import io.searchbox.annotations.JestId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @author Huy Pham
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassifiedAdvertisingElastic {

    @JestId
    private String id;

    private String type;

    private UserProfile author;

    private int locationType;

    private AddressResponse address;

    private List<Image> images;

    private AdditionalInfo additionalInfo;

    private List<String> breadcrumbs;

    private List<AdsMetadata> metadata;

    private Date createdDate;

    private Date modifiedDate;

    private int status;
}

