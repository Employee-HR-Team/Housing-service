package com.beaconfire.housing.housing_service.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateFacilityReportRequest {
    @NotNull(message = "Facility ID is required")
    private Integer facilityId;

    @NotNull(message = "Employee ID is required")
    private Integer employeeId;

    @NotBlank(message = "Report title is required")
    private String title;

    @NotBlank(message = "Report description is required")
    private String description;
}