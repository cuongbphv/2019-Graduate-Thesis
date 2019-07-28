package com.graduate.thesis.backend.controller;

import com.graduate.thesis.backend.entity.*;
import com.graduate.thesis.backend.entity.model.ReportDetail;
import com.graduate.thesis.backend.exception.ApplicationException;
import com.graduate.thesis.backend.model.request.ReportRequest;
import com.graduate.thesis.backend.model.response.RestAPIResponse;
import com.graduate.thesis.backend.model.response.report.PagingReportResponse;
import com.graduate.thesis.backend.model.response.report.ReportDetailResponse;
import com.graduate.thesis.backend.model.response.report.ReportResponse;
import com.graduate.thesis.backend.security.CurrentUser;
import com.graduate.thesis.backend.security.oauth2.user.UserPrincipal;
import com.graduate.thesis.backend.service.*;
import com.graduate.thesis.backend.util.APIStatus;
import com.graduate.thesis.backend.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author cuongbphv created on 21/06/2019
 */

@RestController
@RequestMapping(Constant.REPORT_API)
public class ReportController extends AbstractBasedAPI {

    @Autowired
    ReportService reportService;

    @Autowired
    ClassifiedAdvertisingService classifiedAdvertisingService;

    @Autowired
    UserAccountService userAccountService;

    @Autowired
    UserProfileService userProfileService;

    @Autowired
    RoleService roleService;

    @PostMapping()
    public ResponseEntity<RestAPIResponse> createNewReport (
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody ReportRequest reportRequest
    ) {

        UserAccount user = userAccountService.findActiveUserById(userPrincipal.getId());
        if (user == null) throw new ApplicationException(APIStatus.ERR_USER_NOT_FOUND);

        ClassifiedAdvertising classifiedAdvertising = classifiedAdvertisingService.getClassifiedAdsDetail(
                reportRequest.getClassifiedAdsId(), Arrays.asList(Constant.Status.ACTIVE.getValue()));
        if (classifiedAdvertising == null) {
            throw new ApplicationException(APIStatus.ERR_CLASSIFIED_BLOCKED);
        }

        Report report = reportService.findByClassifiedAdsIdAndStatus(
                reportRequest.getClassifiedAdsId(), Constant.Status.ACTIVE.getValue());

        if (report == null) {
            Date date = new Date();
            report = new Report();
            report.setClassifiedAdsId(reportRequest.getClassifiedAdsId());
            report.setStatus(Constant.Status.ACTIVE.getValue());
            report.setCreatedDate(date);

            List<ReportDetail> reportDetailList = new ArrayList<>();
            reportDetailList.add(new ReportDetail(
                    reportRequest.getReason(),
                    reportRequest.getDescription(),
                    user.getId(),
                    date));
            report.setListReportDetail(reportDetailList);

            reportService.saveReport(report);
            return responseUtil.successResponse("OK");
        }

        List<ReportDetail> reportDetailList = report.getListReportDetail();
        for(ReportDetail reportDetail : reportDetailList) {
            if (reportDetail.getUserId().equals(userPrincipal.getId())) {
                throw new ApplicationException(APIStatus.ERR_USER_REPORTED);
            }
        }

        Date date = new Date();
        reportDetailList.add(new ReportDetail(
                reportRequest.getReason(),
                reportRequest.getDescription(),
                user.getId(),
                date));
        report.setCreatedDate(date);
        reportService.saveReport(report);
        return responseUtil.successResponse("OK");
    }

    @GetMapping(Constant.GET_NUMBER_ITEM)
    public ResponseEntity<RestAPIResponse> getNumberReports (
            @RequestParam("id") String id
    ) {
        Report report = reportService.findByClassifiedAdsIdAndStatus(
                id, Constant.Status.ACTIVE.getValue());

        if (report == null) return responseUtil.successResponse(0);

        return responseUtil.successResponse(report.getListReportDetail().size());
    }

    @GetMapping(Constant.GET_LIST)
    public ResponseEntity<RestAPIResponse> getPagingReport (
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(value = "asc_sort") boolean ascSort,
            @RequestParam("page_number") int pageNumber,
            @RequestParam("page_size") int pageSize
    ) {

        Page<Report> page = reportService.findByStatus(Constant.Status.ACTIVE.getValue(),
                ascSort, pageNumber, pageSize);

        List<ReportResponse> content = new ArrayList<>();
        for (Report report : page.getContent()) {
            ReportResponse reportResponse = new ReportResponse();
            reportResponse.setId(report.getId());
            reportResponse.setClassifiedAdsId(report.getClassifiedAdsId());
            reportResponse.setCreatedDate(report.getCreatedDate());
            reportResponse.setStatus(report.getStatus());

            // get classified advertising
            ClassifiedAdvertising classifiedAdvertising = classifiedAdvertisingService.getClassifiedAdsDetail(
                    report.getClassifiedAdsId(), Arrays.asList(Constant.Status.ACTIVE.getValue()));
            reportResponse.setTitle(classifiedAdvertising.getAdditionalInfo().getTitle());
            reportResponse.setAuthorId(classifiedAdvertising.getAuthorId());

            UserProfile profile = userProfileService.findByUserId(userPrincipal.getId())
                    .orElseThrow(() -> new ApplicationException(APIStatus.ERR_USER_PROFILE_NOT_FOUND));

            reportResponse.setFirstName(profile.getFirstName());
            reportResponse.setLastName(profile.getLastName());
            reportResponse.setCreatedDateAds(classifiedAdvertising.getCreatedDate());

            List<ReportDetailResponse> reportDetailResponseList = new ArrayList<>();
            for (ReportDetail reportDetail : report.getListReportDetail()) {
                ReportDetailResponse reportDetailReponse = new ReportDetailResponse();
                reportDetailReponse.setCreatedDate(reportDetail.getCreatedDate());
                reportDetailReponse.setDescription(reportDetail.getDescription());
                reportDetailReponse.setReason(reportDetail.getReason());
                reportDetailReponse.setUserId(reportDetail.getUserId());
                UserProfile reportProfile = userProfileService.findByUserId(userPrincipal.getId())
                        .orElseThrow(() -> new ApplicationException(APIStatus.ERR_USER_PROFILE_NOT_FOUND));
                reportDetailReponse.setFirstName(reportProfile.getFirstName());
                reportDetailReponse.setLastName(reportProfile.getLastName());
                reportDetailResponseList.add(reportDetailReponse);
            }

            reportResponse.setListReportDetail(reportDetailResponseList);
            content.add(reportResponse);
        }

        PagingReportResponse response = new PagingReportResponse();
        response.setContent(content);
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        return responseUtil.successResponse(response);
    }

    @PutMapping()
    public ResponseEntity<RestAPIResponse> getPagingReport (
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody ReportRequest reportRequest
    ) {
        UserAccount user = userAccountService.findActiveUserById(userPrincipal.getId());
        if (user == null) throw new ApplicationException(APIStatus.ERR_USER_NOT_FOUND);

        Role loginRole = roleService.findByRoleId(user.getRoleId());
        // check role admin or moderator can see classified advertising
        if (!loginRole.getName().equals(Constant.Role.ADMIN.getName())
                && !loginRole.getName().equals(Constant.Role.MODERATOR.getName())) {
            throw new ApplicationException(APIStatus.ERR_UNAUTHORIZED);
        }

        Report report = reportService.findByClassifiedAdsIdAndStatus(
                reportRequest.getClassifiedAdsId(), Constant.Status.ACTIVE.getValue());
        if (report == null) throw new ApplicationException(APIStatus.ERR_BAD_REQUEST);

        ClassifiedAdvertising classifiedAdvertising = classifiedAdvertisingService.getClassifiedAdsDetail(
                reportRequest.getClassifiedAdsId(), Arrays.asList(Constant.Status.ACTIVE.getValue()));
        if (classifiedAdvertising == null) {
            throw new ApplicationException(APIStatus.ERR_CLASSIFIED_ADVERTISING_NOT_FOUND);
        }

        if (reportRequest.getStatus() == Constant.Status.DEACTIVE.getValue()) {
            report.setStatus(Constant.Status.DELETE.getValue());
            reportService.saveReport(report);
        } else if (reportRequest.getStatus() == Constant.Status.BLOCK.getValue()) {
            report.setStatus(Constant.Status.DELETE.getValue());
            reportService.saveReport(report);
            classifiedAdvertising.setStatus(Constant.Status.BLOCK.getValue());
            classifiedAdvertisingService.save(classifiedAdvertising);
        } else if (reportRequest.getStatus() == Constant.Status.DELETE.getValue()) {
            report.setStatus(Constant.Status.DELETE.getValue());
            reportService.saveReport(report);
            classifiedAdvertising.setStatus(Constant.Status.DELETE.getValue());
            classifiedAdvertisingService.save(classifiedAdvertising);
        }

        return responseUtil.successResponse("OK");
    }
}
