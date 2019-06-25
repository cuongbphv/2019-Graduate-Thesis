package com.graduate.thesis.backend.model.response.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @author cuongbphv created on 25/06/2019
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportDetailResponse {

    private String reason;

    private String description;

    private String userId;

    private String firstName;

    private String lastName;

    private Date createdDate;

}
