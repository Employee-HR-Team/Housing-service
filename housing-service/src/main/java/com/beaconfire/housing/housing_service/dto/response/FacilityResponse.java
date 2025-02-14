package com.beaconfire.housing.housing_service.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacilityResponse {
    private Integer id;
    private String type;
    private String description;
    private Integer quantity;
    private LocalDateTime createDate;
    private LocalDateTime lastModificationDate;
    
    // House information
    private HouseInfo house;
    
    // Report summary (if any reports exist)
    private ReportSummary reportSummary;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HouseInfo {
        private Integer id;
        private String address;
        private Integer maxOccupant;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReportSummary {
        private Integer totalReports;
        private Integer openReports;
        private Integer inProgressReports;
        private Integer closedReports;
    }
}