package com.graduate.thesis.backend.model.response.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author cuongbphv created on 25/06/2019
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagingReportResponse {

    private List<ReportResponse> content;

    private long totalElements;

    private int totalPages;

}
