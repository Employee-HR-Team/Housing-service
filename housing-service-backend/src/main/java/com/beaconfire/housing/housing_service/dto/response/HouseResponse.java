package com.beaconfire.housing.housing_service.dto.response;


import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HouseResponse {
    private Integer id;
    private String address;
    private Integer maxOccupant;

    // Landlord information
    private String landlordFirstName;
    private String landlordLastName;
    private String landlordPhone;
    private String landlordEmail;

    // Facility information
    private Map<String, Integer> facilities; // type -> quantity

    // Employee information
    private List<ResidentInfo> residents;

    // Facility reports
    private List<FacilityReportInfo> facilityReports;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResidentInfo {
        private String preferredName;
        private String phoneNumber;
        private String email;
        private String carInformation;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FacilityReportInfo {
        private Integer id;
        private String title;
        private String description;
        private String createdBy;
        private String timestamp;
        private String status;
        private List<CommentInfo> comments;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentInfo {
        private String description;
        private String author;
        private String timestamp;
    }
}