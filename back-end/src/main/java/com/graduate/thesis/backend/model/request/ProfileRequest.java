package com.graduate.thesis.backend.model.request;

import com.graduate.thesis.backend.entity.Address;
import com.graduate.thesis.backend.model.SocialDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.List;

/**
 * @author Huy Pham
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequest {

    private String userProfileId;

    private String userId;

    private String firstName;

    private String lastName;

    private int gender;

    private String dob;

    private String avatarUrl;

    private String description;

    private List<SocialDetail> socialLink;

    private List<Address> address;

    private String setting;

    private int type;
}
