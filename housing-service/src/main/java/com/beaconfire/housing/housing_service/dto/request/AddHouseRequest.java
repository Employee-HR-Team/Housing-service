package com.beaconfire.housing.housing_service.dto.request;

import java.util.Map;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddHouseRequest {
    // Landlord Information
    @NotBlank(message = "Landlord first name is required")
    private String landlordFirstName;

    @NotBlank(message = "Landlord last name is required")
    private String landlordLastName;

    @NotBlank(message = "Landlord email is required")
    private String landlordEmail;

    @NotBlank(message = "Landlord phone number is required")
    private String landlordPhone;

    // House Information
    @NotBlank(message = "House address is required")
    private String address;

    @NotNull(message = "Maximum occupant number is required")
    @Min(value = 1, message = "Maximum occupant must be at least 1")
    private Integer maxOccupant;

    // Basic Facility Information
    private Map<String, Integer> facilities; // Map of facility type to quantity (e.g., "bed" -> 4)
}