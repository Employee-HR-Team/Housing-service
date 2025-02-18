package com.beaconfire.housing.housing_service.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.beaconfire.housing.housing_service.dto.request.CreateFacilityReportCommentRequest;
import com.beaconfire.housing.housing_service.dto.request.CreateFacilityReportRequest;
import com.beaconfire.housing.housing_service.dto.response.FacilityReportResponse;
import com.beaconfire.housing.housing_service.entity.FacilityReport;
import com.beaconfire.housing.housing_service.service.FacilityReportService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/facility-reports")
@RequiredArgsConstructor
@Api(tags = "Facility Report Management")
public class FacilityReportController {
    private final FacilityReportService facilityReportService;

    @PostMapping
    @ApiOperation("Create a new facility report")
    public ResponseEntity<FacilityReportResponse> createReport(
            @Valid @RequestBody CreateFacilityReportRequest request) {
        return ResponseEntity.ok(facilityReportService.createReport(request));
    }

    @GetMapping
    @ApiOperation("Get all facility reports")
    public ResponseEntity<List<FacilityReportResponse>> getAllFacilityReports() {
        return ResponseEntity.ok(facilityReportService.getAllFacilityReports());
    }

    @PostMapping("/{reportId}/comments")
    @ApiOperation("Add a comment to a facility report")
    public ResponseEntity<FacilityReportResponse> addComment(
            @Valid @RequestBody CreateFacilityReportCommentRequest request) {
        return ResponseEntity.ok(facilityReportService.addComment(request));
    }

    @GetMapping("/{reportId}")
    @ApiOperation("Get facility report by ID")
    public ResponseEntity<FacilityReportResponse> getReport(@PathVariable Integer reportId) {
        return ResponseEntity.ok(facilityReportService.getReport(reportId));
    }

    @PutMapping("/{reportId}/status")
    @ApiOperation("Update facility report status")
    public ResponseEntity<FacilityReportResponse> updateStatus(
            @PathVariable Integer reportId,
            @RequestParam FacilityReport.ReportStatus status) {
        return ResponseEntity.ok(facilityReportService.updateReportStatus(reportId, status));
    }

    @GetMapping("/facility/{facilityId}")
    @ApiOperation("Get facility reports by facility ID")
    public ResponseEntity<List<FacilityReportResponse>> getReportsByFacility(
            @PathVariable Integer facilityId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(facilityReportService.getReportsByFacility(facilityId, page, size));
    }

    @GetMapping("/status/{status}")
    @ApiOperation("Get facility reports by status")
    public ResponseEntity<List<FacilityReportResponse>> getReportsByStatus(
            @PathVariable FacilityReport.ReportStatus status) {
        return ResponseEntity.ok(facilityReportService.getReportsByStatus(status));
    }

    @GetMapping("/employee/{employeeId}")
    @ApiOperation("Get facility reports by employee ID")
    public ResponseEntity<List<FacilityReportResponse>> getReportsByEmployee(
            @PathVariable Integer employeeId) {
        return ResponseEntity.ok(facilityReportService.getReportsByEmployee(employeeId));
    }

    @GetMapping("/house/{houseId}")
    @ApiOperation("Get facility reports by house ID")
    public ResponseEntity<List<FacilityReportResponse>> getReportsByHouse(
            @PathVariable Integer houseId) {
        return ResponseEntity.ok(facilityReportService.getReportsByHouseId(houseId));
    }

    @PutMapping("/comments/{commentId}")
    @ApiOperation("Edit a comment")
    public ResponseEntity<FacilityReportResponse> editComment(
            @PathVariable Integer commentId,
            @RequestParam Integer employeeId,
            @RequestParam String newComment) {
        return ResponseEntity.ok(facilityReportService.editComment(commentId, employeeId, newComment));
    }

    @DeleteMapping("/{reportId}")
    @ApiOperation("Delete a facility report")
    public ResponseEntity<Void> deleteReport(
            @PathVariable Integer reportId,
            @RequestParam Integer employeeId) {
        facilityReportService.deleteReport(reportId, employeeId);
        return ResponseEntity.noContent().build();
    }
}