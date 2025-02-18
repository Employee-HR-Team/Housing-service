package com.beaconfire.housing.housing_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse {
    private String id;
    private String userId;
    private String firstName;
    private String lastName;
    private String middleName;
    private String preferredName;
    private String email;
    private String phoneNumber;
    private String cellPhone;
    private String alternatePhone;
    private String gender;
    private String ssn;
    private String dob;
    private String startDate;
    private String endDate;
    private String driverLicense;
    private String driverLicenseExpiration;
    private String houseId;
}