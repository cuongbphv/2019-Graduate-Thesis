package com.graduate.thesis.backend.service;

import com.graduate.thesis.backend.entity.Report;
import com.graduate.thesis.backend.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * @author cuongbphv created on 21/06/2019
 */
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    ReportRepository reportRepository;

    @Override
    public void saveReport(Report report) {
        reportRepository.save(report);
    }

    @Override
    public Report findByClassifiedAdsIdAndStatus(String classifiedAdsId, int status) {
        return reportRepository.findByClassifiedAdsIdAndStatus(classifiedAdsId, status);
    }

    @Override
    public Page<Report> findByStatus(int status, boolean ascSort, int pageNumber, int pageSize) {
        Pageable pageable = new PageRequest(pageNumber - 1, pageSize,
                new Sort(ascSort ? Sort.Direction.ASC : Sort.Direction.DESC, "createdDate"));
        return reportRepository.findByStatus(status, pageable);
    }
}
