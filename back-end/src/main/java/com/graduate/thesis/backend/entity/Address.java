package com.graduate.thesis.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Huy Pham
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document("address")
public class Address {

    @Id
    private String id;

    private String userId;

    private String provinceId;

    private String districtId;

    private String wardId;

    private String detail;

    private boolean defaultAddress;

    private int status;

}
