package com.beaconfire.housing.housing_service.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LandlordResponse {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String cellPhone;
    
    // Houses managed by this landlord
    private List<HouseInfo> houses;
    
    // Summary statistics
    private Statistics statistics;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HouseInfo {
        private Integer id;
        private String address;
        private Integer currentOccupants;
        private Integer maxOccupant;
        private Integer openFacilityReports;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Statistics {
        private Integer totalHouses;
        private Integer totalResidents;
        private Integer totalFacilities;
        private Integer totalOpenReports;
    }
}
