package com.graduate.thesis.backend.repository;

import com.graduate.thesis.backend.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author cuongbphv created on 21/06/2019
 */
@Repository
public interface ReportRepository extends MongoRepository<Report, String> {

    Report findByClassifiedAdsIdAndStatus(String classifiedAdsId, int status);

    Page<Report> findByStatus(int status, Pageable pageable);
}
