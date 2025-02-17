package com.beaconfire.housing.housing_service.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateFacilityReportCommentRequest {
    @NotNull(message = "Facility report ID is required")
    private Integer facilityReportId;

    @NotNull(message = "Employee ID is required")
    private Integer employeeId;

    @NotBlank(message = "Comment content is required")
    private String comment;
}
