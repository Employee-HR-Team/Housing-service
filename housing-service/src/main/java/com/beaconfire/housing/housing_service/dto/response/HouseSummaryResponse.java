package com.beaconfire.housing.housing_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HouseSummaryResponse {
    private Integer id;
    private String address;
    
    // Landlord information
    private LandlordInfo landlord;
    
    private Integer numberOfResidents;
    private Integer maxOccupant;
    
    // Basic facility counts
    private FacilityCount facilityCount;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LandlordInfo {
        private String fullName;
        private String phoneNumber;
        private String email;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FacilityCount {
        private Integer beds;
        private Integer mattresses;
        private Integer tables;
        private Integer chairs;
    }
}
