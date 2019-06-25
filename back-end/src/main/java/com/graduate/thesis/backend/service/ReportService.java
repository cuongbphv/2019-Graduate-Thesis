package com.graduate.thesis.backend.service;

import com.graduate.thesis.backend.entity.Report;
import org.springframework.data.domain.Page;

/**
 * @author cuongbphv created on 21/06/2019
 */
public interface ReportService {

    void saveReport(Report report);

    Report findByClassifiedAdsIdAndStatus(String classifiedAdsId, int status);

    Page<Report> findByStatus(int status, boolean ascSort, int pageNumber, int pageSize);

}
