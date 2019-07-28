package com.graduate.thesis.backend.entity;

import com.graduate.thesis.backend.entity.model.ReportDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * @author cuongbphv created on 21/06/2019
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document("report")
public class Report {

    @Id
    private String id;

    private String classifiedAdsId;

    private List<ReportDetail> listReportDetail;

    private Date createdDate;

    private int status;

}
