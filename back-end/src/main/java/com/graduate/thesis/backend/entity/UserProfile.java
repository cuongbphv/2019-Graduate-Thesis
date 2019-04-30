package com.graduate.thesis.backend.entity;

import com.graduate.thesis.backend.entity.model.SocialDetail;
import com.graduate.thesis.backend.entity.model.UserSetting;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * @author Huy Pham
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document("user_profile")
public class UserProfile {

    @Id
    private String id;

    private String userId;

    private String firstName;

    private String lastName;

    private int gender;

    private Date dob;

    private String avatarUrl;

    private String description;

    private List<SocialDetail> socialLink;

    private List<Address> address;

    private UserSetting setting;

    private int type;

    private int status;

    @CreatedDate
    private Date createdDate;

    @LastModifiedDate
    private Date modifiedDate;

    private List<String> reportedBy;

    private List<String> reported;

    private List<String> followedBy;

    private List<String> following;

}
