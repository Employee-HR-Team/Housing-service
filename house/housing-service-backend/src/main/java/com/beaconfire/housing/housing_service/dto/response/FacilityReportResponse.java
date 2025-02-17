package com.beaconfire.housing.housing_service.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacilityReportResponse {
    private Integer id;
    private String title;
    private String description;
    private String status;
    private Integer employeeId;
    private String employeeName;  // Will be populated using EmployeeClient
    private LocalDateTime createDate;
    
    // Facility information
    private FacilityInfo facility;
    
    // Comments
    private List<CommentInfo> comments;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FacilityInfo {
        private Integer id;
        private String type;
        private String description;
        private Integer quantity;
        private String houseAddress;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentInfo {
        private Integer id;
        private String comment;
        private Integer employeeId;
        private String employeeName;  // Will be populated using EmployeeClient
        private LocalDateTime createDate;
        private LocalDateTime lastModificationDate;
    }
}