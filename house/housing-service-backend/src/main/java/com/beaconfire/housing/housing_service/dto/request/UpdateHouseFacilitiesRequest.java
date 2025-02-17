package com.beaconfire.housing.housing_service.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateHouseFacilitiesRequest {
    @NotNull(message = "Number of beds is required")
    @Min(value = 0, message = "Number of beds cannot be negative")
    private Integer beds;

    @NotNull(message = "Number of mattresses is required")
    @Min(value = 0, message = "Number of mattresses cannot be negative")
    private Integer mattresses;

    @NotNull(message = "Number of tables is required")
    @Min(value = 0, message = "Number of tables cannot be negative")
    private Integer tables;

    @NotNull(message = "Number of chairs is required")
    @Min(value = 0, message = "Number of chairs cannot be negative")
    private Integer chairs;
}