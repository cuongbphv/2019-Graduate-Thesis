package com.graduate.thesis.backend.model.response.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @author cuongbphv created on 25/06/2019
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponse {

    private String id;

    private String classifiedAdsId;

    private String authorId;
    private String firstName;
    private String lastName;

    private String title;

    private Date createdDateAds;

    private List<ReportDetailResponse> listReportDetail;

    private Date createdDate;

    private int status;
}
