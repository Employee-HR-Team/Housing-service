package com.beaconfire.housing.housing_service.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.beaconfire.housing.housing_service.client.EmployeeClient;
import com.beaconfire.housing.housing_service.dto.request.CreateFacilityReportCommentRequest;
import com.beaconfire.housing.housing_service.dto.request.CreateFacilityReportRequest;
import com.beaconfire.housing.housing_service.dto.response.FacilityReportResponse;
import com.beaconfire.housing.housing_service.entity.Facility;
import com.beaconfire.housing.housing_service.entity.FacilityReport;
import com.beaconfire.housing.housing_service.entity.FacilityReportDetail;
import com.beaconfire.housing.housing_service.entity.House;
import com.beaconfire.housing.housing_service.exception.CommentNotFoundException;
import com.beaconfire.housing.housing_service.exception.FacilityNotFoundException;
import com.beaconfire.housing.housing_service.exception.FacilityReportNotFoundException;
import com.beaconfire.housing.housing_service.exception.HouseNotFoundException;
import com.beaconfire.housing.housing_service.repository.FacilityReportDetailRepository;
import com.beaconfire.housing.housing_service.repository.FacilityReportRepository;
import com.beaconfire.housing.housing_service.repository.FacilityRepository;
import com.beaconfire.housing.housing_service.repository.HouseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FacilityReportService {
    private final FacilityRepository facilityRepository;
    private final FacilityReportRepository reportRepository;
    private final FacilityReportDetailRepository reportDetailRepository;
    private final EmployeeClient employeeClient;
    private final HouseRepository houseRepository;  

    @Transactional
    public FacilityReportResponse createReport(CreateFacilityReportRequest request) {
        Facility facility = facilityRepository.findById(request.getFacilityId());
        if (facility == null) {
            throw new FacilityNotFoundException("Facility not found with id: " + request.getFacilityId());
        }

        FacilityReport report = new FacilityReport();
        report.setFacility(facility);
        report.setEmployeeId(request.getEmployeeId());
        report.setTitle(request.getTitle());
        report.setDescription(request.getDescription());
        report.setStatus(FacilityReport.ReportStatus.Open);
        
        report = reportRepository.save(report);
        return buildReportResponse(report);
    }

    @Transactional
    public FacilityReportResponse addComment(CreateFacilityReportCommentRequest request) {
        FacilityReport report = reportRepository.findById(request.getFacilityReportId());
        if (report == null) {
            throw new FacilityReportNotFoundException("Report not found with id: " + request.getFacilityReportId());
        }

        FacilityReportDetail comment = new FacilityReportDetail();
        comment.setFacilityReport(report);
        comment.setEmployeeId(request.getEmployeeId());
        comment.setComment(request.getComment());
        
        reportDetailRepository.save(comment);
        return buildReportResponse(report);
    }
    
    @Transactional
    public FacilityReportResponse getReport(Integer reportId) {
        FacilityReport report = reportRepository.findById(reportId);
        if (report == null) {
            throw new FacilityReportNotFoundException("Report not found with id: " + reportId);
        }
        return buildReportResponse(report);
    }

    @Transactional
    public FacilityReportResponse updateReportStatus(Integer reportId, FacilityReport.ReportStatus newStatus) {
        FacilityReport report = reportRepository.findById(reportId);
        if (report == null) {
            throw new FacilityReportNotFoundException("Report not found with id: " + reportId);
        }
        
        report.setStatus(newStatus);
        report = reportRepository.save(report);
        return buildReportResponse(report);
    }

    public List<FacilityReportResponse> getReportsByFacility(Integer facilityId, int page, int size) {
        Facility facility = facilityRepository.findById(facilityId);
        if (facility == null) {
            throw new FacilityNotFoundException("Facility not found with id: " + facilityId);
        }
        
        return reportRepository.findByFacility(facility, page * size, size).stream()
                .map(this::buildReportResponse)
                .collect(Collectors.toList());
    }

    public List<FacilityReportResponse> getReportsByStatus(FacilityReport.ReportStatus status) {
        return reportRepository.findByStatus(status).stream()
                .map(this::buildReportResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public FacilityReportResponse editComment(Integer commentId, Integer employeeId, String newComment) {
        FacilityReportDetail comment = reportDetailRepository.findById(commentId);
        if (comment == null) {
            throw new CommentNotFoundException("Comment not found with id: " + commentId);
        }

        if (!comment.getEmployeeId().equals(employeeId)) {
            throw new RuntimeException("Unauthorized to edit this comment");
        }

        comment.setComment(newComment);
        comment.setLastModificationDate(LocalDateTime.now());
        reportDetailRepository.save(comment);

        return buildReportResponse(comment.getFacilityReport());
    }

    private FacilityReportResponse buildReportResponse(FacilityReport report) {
        FacilityReportResponse.FacilityInfo facilityInfo = new FacilityReportResponse.FacilityInfo(
                report.getFacility().getId(),
                report.getFacility().getType(),
                report.getFacility().getDescription(),
                report.getFacility().getQuantity(),
                report.getFacility().getHouse().getAddress()
        );

        List<FacilityReportResponse.CommentInfo> comments = reportDetailRepository
                .findByFacilityReportOrderByCreateDateDesc(report)
                .stream()
                .map(detail -> new FacilityReportResponse.CommentInfo(
                        detail.getId(),
                        detail.getComment(),
                        detail.getEmployeeId(),
                        getEmployeeName(detail.getEmployeeId()),
                        detail.getCreateDate(),
                        detail.getLastModificationDate()
                ))
                .collect(Collectors.toList());

        return FacilityReportResponse.builder()
                .id(report.getId())
                .title(report.getTitle())
                .description(report.getDescription())
                .status(report.getStatus().toString())
                .employeeId(report.getEmployeeId())
                .employeeName(getEmployeeName(report.getEmployeeId()))
                .createDate(report.getCreateDate())
                .facility(facilityInfo)
                .comments(comments)
                .build();
    }

    private String getEmployeeName(Integer employeeId) {
        // Implementation for getting employee name using EmployeeClient
        return "Employee " + employeeId; // Placeholder
    }
    

    public List<FacilityReportResponse> getReportsByEmployee(Integer employeeId) {
        return reportRepository.findByEmployeeId(employeeId).stream()
                .map(this::buildReportResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<FacilityReportResponse> getReportsByHouseId(Integer houseId) {
        House house = houseRepository.findById(houseId);
        if (house == null) {
            throw new HouseNotFoundException("House not found with id: " + houseId);
        }

        // Get all facilities for this house
        List<Facility> facilities = facilityRepository.findByHouse(house);
    
        // Get reports for all facilities
        return facilities.stream()
            .flatMap(facility -> reportRepository.findByFacility(facility).stream())
            .map(this::buildReportResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional 
    public List<FacilityReportResponse> getAllFacilityReports() {
        List<FacilityReport> reports = reportRepository.findAll();
        return reports.stream()
                .map(this::buildReportResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteReport(Integer reportId, Integer employeeId) {
        FacilityReport report = reportRepository.findById(reportId);
        if (report == null) {
            throw new FacilityReportNotFoundException("Report not found with id: " + reportId);
        }

        if (!report.getEmployeeId().equals(employeeId)) {
            throw new RuntimeException("Unauthorized to delete this report");
        }

        // Delete all comments first
        List<FacilityReportDetail> comments = reportDetailRepository.findByFacilityReport(report);
        for (FacilityReportDetail comment : comments) {
            reportDetailRepository.delete(comment);
        }

        // Then delete the report
        reportRepository.delete(report);
    }
}