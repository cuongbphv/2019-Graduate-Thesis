package com.graduate.thesis.backend.entity.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @author cuongbphv created on 24/06/2019
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportDetail {

    private String reason;

    private String description;

    private String userId;

    private Date createdDate;

}
