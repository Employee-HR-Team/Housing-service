package com.beaconfire.housing.housing_service.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.beaconfire.housing.housing_service.client.EmployeeClient;
import com.beaconfire.housing.housing_service.dto.request.AddHouseRequest;
import com.beaconfire.housing.housing_service.dto.request.UpdateHouseFacilitiesRequest;
import com.beaconfire.housing.housing_service.dto.response.FacilityResponse;
import com.beaconfire.housing.housing_service.dto.response.HouseResponse;
import com.beaconfire.housing.housing_service.dto.response.HouseSummaryResponse;
import com.beaconfire.housing.housing_service.entity.Facility;
import com.beaconfire.housing.housing_service.entity.FacilityReport;
import com.beaconfire.housing.housing_service.entity.House;
import com.beaconfire.housing.housing_service.entity.Landlord;
import com.beaconfire.housing.housing_service.exception.HouseNotFoundException;
import com.beaconfire.housing.housing_service.repository.FacilityReportRepository;
import com.beaconfire.housing.housing_service.repository.FacilityRepository;
import com.beaconfire.housing.housing_service.repository.HouseRepository;
import com.beaconfire.housing.housing_service.repository.LandlordRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HouseService {
    private final HouseRepository houseRepository;
    private final LandlordRepository landlordRepository;
    private final FacilityRepository facilityRepository;
    private final FacilityReportRepository facilityReportRepository;
    private final EmployeeClient employeeClient;

    @Transactional
    public HouseResponse addHouse(AddHouseRequest request) {
        // Create or get landlord
        Landlord landlord = null;
        Landlord existingLandlord = landlordRepository.findByEmail(request.getLandlordEmail());
        
        if (existingLandlord == null) {
            landlord = new Landlord();
            landlord.setFirstName(request.getLandlordFirstName());
            landlord.setLastName(request.getLandlordLastName());
            landlord.setEmail(request.getLandlordEmail());
            landlord.setCellPhone(request.getLandlordPhone());
            landlord = landlordRepository.save(landlord);
        } else {
            landlord = existingLandlord;
        }

        // Create house
        House house = new House();
        house.setLandlord(landlord);
        house.setAddress(request.getAddress());
        house.setMaxOccupant(request.getMaxOccupant());
        house = houseRepository.save(house);

        // Add facilities
        if (request.getFacilities() != null) {
            for (Map.Entry<String, Integer> entry : request.getFacilities().entrySet()) {
                Facility facility = new Facility();
                facility.setHouse(house);
                facility.setType(entry.getKey());
                facility.setQuantity(entry.getValue());
                facilityRepository.save(facility);
            }
        }

        return getHouseResponse(house.getId());
    }

    @Transactional
    public HouseResponse getHouseResponse(Integer houseId) {
        House house = houseRepository.findById(houseId);
        if (house == null) {
            throw new HouseNotFoundException("House not found with id: " + houseId);
        }
        return buildHouseResponse(house);
    }

    @Transactional
    public List<HouseSummaryResponse> getAllHouseSummaries() {
        return houseRepository.findAll().stream()
                .map(this::buildHouseSummaryResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public HouseSummaryResponse getHouseSummary(Integer houseId) {
        House house = houseRepository.findById(houseId);
        if (house == null) {
            throw new HouseNotFoundException("House not found with id: " + houseId);
        }
        return buildHouseSummaryResponse(house);
    }

    @Transactional
    public void deleteHouse(Integer houseId) {
        House house = houseRepository.findById(houseId);
        if (house == null) {
            throw new HouseNotFoundException("House not found with id: " + houseId);
        }

        // Delete all related facilities and reports first
        List<Facility> facilities = facilityRepository.findByHouse(house);
        for (Facility facility : facilities) {
            // Delete all reports for this facility
            List<FacilityReport> reports = facilityReportRepository.findByFacility(facility);
            for (FacilityReport report : reports) {
                facilityReportRepository.delete(report);
            }
            // Then delete the facility
            facilityRepository.delete(facility);
        }

        // Finally delete the house
        houseRepository.delete(house);
    }
    @Transactional
    public HouseResponse updateHouse(Integer houseId, AddHouseRequest request) {
        House house = houseRepository.findById(houseId);
        if (house == null) {
            throw new HouseNotFoundException("House not found with id: " + houseId);
        }

        // Update house details
        house.setAddress(request.getAddress());
        house.setMaxOccupant(request.getMaxOccupant());

        // Update landlord details
        Landlord landlord = house.getLandlord();
        landlord.setFirstName(request.getLandlordFirstName());
        landlord.setLastName(request.getLandlordLastName());
        landlord.setEmail(request.getLandlordEmail());
        landlord.setCellPhone(request.getLandlordPhone());
        landlordRepository.save(landlord);

        // Update facilities
        List<Facility> existingFacilities = facilityRepository.findByHouse(house);
        for (Facility facility : existingFacilities) {
            facilityRepository.delete(facility);
        }

        if (request.getFacilities() != null) {
            for (Map.Entry<String, Integer> entry : request.getFacilities().entrySet()) {
                Facility facility = new Facility();
                facility.setHouse(house);
                facility.setType(entry.getKey());
                facility.setQuantity(entry.getValue());
                facilityRepository.save(facility);
            }
        }

        house = houseRepository.save(house);
        return buildHouseResponse(house);
    }

    private HouseResponse buildHouseResponse(House house) {
        List<Facility> facilities = facilityRepository.findByHouse(house);
        Map<String, Integer> facilityMap = facilities.stream()
                .collect(Collectors.toMap(
                        Facility::getType,
                        Facility::getQuantity,
                        (existing, replacement) -> existing
                ));

        // Get facility reports
        List<HouseResponse.FacilityReportInfo> reportInfos = new ArrayList<>();
        for (Facility facility : facilities) {
            List<FacilityReport> reports = facilityReportRepository.findByFacility(facility);
            for (FacilityReport report : reports) {
                List<HouseResponse.CommentInfo> comments = report.getReportDetails().stream()
                        .map(detail -> new HouseResponse.CommentInfo(
                                detail.getComment(),
                                getEmployeeName(detail.getEmployeeId()),
                                detail.getCreateDate().toString()
                        ))
                        .collect(Collectors.toList());

                reportInfos.add(new HouseResponse.FacilityReportInfo(
                        report.getId(),
                        report.getTitle(),
                        report.getDescription(),
                        getEmployeeName(report.getEmployeeId()),
                        report.getCreateDate().toString(),
                        report.getStatus().toString(),
                        comments
                ));
            }
        }

        // Get resident information from employee service
        List<HouseResponse.ResidentInfo> residents = new ArrayList<>();
        try {
            // TODO: Implement EmployeeClient call to get residents
            // residents = employeeClient.getResidentsByHouseId(house.getId());
        } catch (Exception e) {
            // Log error but continue with empty residents list
        }

        return HouseResponse.builder()
                .id(house.getId())
                .address(house.getAddress())
                .maxOccupant(house.getMaxOccupant())
                .landlordFirstName(house.getLandlord().getFirstName())
                .landlordLastName(house.getLandlord().getLastName())
                .landlordPhone(house.getLandlord().getCellPhone())
                .landlordEmail(house.getLandlord().getEmail())
                .facilities(facilityMap)
                .residents(residents)
                .facilityReports(reportInfos)
                .build();
    }

    private HouseSummaryResponse buildHouseSummaryResponse(House house) {
        List<Facility> facilities = facilityRepository.findByHouse(house);
        
        HouseSummaryResponse.FacilityCount facilityCount = new HouseSummaryResponse.FacilityCount(
                countFacilityByType(facilities, "bed"),
                countFacilityByType(facilities, "mattress"),
                countFacilityByType(facilities, "table"),
                countFacilityByType(facilities, "chair")
        );

        HouseSummaryResponse.LandlordInfo landlordInfo = new HouseSummaryResponse.LandlordInfo(
                house.getLandlord().getFirstName() + " " + house.getLandlord().getLastName(),
                house.getLandlord().getCellPhone(),
                house.getLandlord().getEmail()
        );

        return HouseSummaryResponse.builder()
                .id(house.getId())
                .address(house.getAddress())
                .landlord(landlordInfo)
                .numberOfResidents(getCurrentResidentCount(house))
                .maxOccupant(house.getMaxOccupant())
                .facilityCount(facilityCount)
                .build();
    }

    private Integer countFacilityByType(List<Facility> facilities, String type) {
        return facilities.stream()
                .filter(f -> f.getType().equalsIgnoreCase(type))
                .mapToInt(Facility::getQuantity)
                .sum();
    }

    private Integer getCurrentResidentCount(House house) {
        try {
            // TODO: Implement EmployeeClient call to get resident count
            return 0; // Placeholder
        } catch (Exception e) {
            return 0;
        }
    }

    @Transactional
    public List<FacilityResponse> getHouseFacilities(Integer houseId) {
        House house = houseRepository.findById(houseId);
        if (house == null) {
            throw new HouseNotFoundException("House not found with id: " + houseId);
        }
    
    List<Facility> facilities = facilityRepository.findByHouse(house);
    return facilities.stream()
            .map(facility -> {
                // Create house info
                FacilityResponse.HouseInfo houseInfo = new FacilityResponse.HouseInfo(
                    house.getId(),
                    house.getAddress(),
                    house.getMaxOccupant()
                );

                // Get facility reports statistics
                List<FacilityReport> reports = facilityReportRepository.findByFacility(facility);
                int totalReports = reports.size();
                int openReports = (int) reports.stream()
                    .filter(r -> r.getStatus() == FacilityReport.ReportStatus.Open)
                    .count();
                int inProgressReports = (int) reports.stream()
                    .filter(r -> r.getStatus() == FacilityReport.ReportStatus.InProgress)
                    .count();
                int closedReports = (int) reports.stream()
                    .filter(r -> r.getStatus() == FacilityReport.ReportStatus.Closed)
                    .count();

                FacilityResponse.ReportSummary reportSummary = new FacilityResponse.ReportSummary(
                    totalReports,
                    openReports,
                    inProgressReports,
                    closedReports
                );

                return FacilityResponse.builder()
                    .id(facility.getId())
                    .type(facility.getType())
                    .description(facility.getDescription())
                    .quantity(facility.getQuantity())
                    .createDate(facility.getCreateDate())
                    .lastModificationDate(facility.getLastModificationDate())
                    .house(houseInfo)
                    .reportSummary(reportSummary)
                    .build();
            })
            .collect(Collectors.toList());
}

@Transactional
public List<FacilityResponse> updateHouseFacilities(Integer houseId, UpdateHouseFacilitiesRequest request) {
    House house = houseRepository.findById(houseId);
    if (house == null) {
        throw new HouseNotFoundException("House not found with id: " + houseId);
    }

    // Delete existing facilities
    List<Facility> existingFacilities = facilityRepository.findByHouse(house);
    for (Facility facility : existingFacilities) {
        List<FacilityReport> reports = facilityReportRepository.findByFacility(facility);
        for (FacilityReport report : reports) {
            facilityReportRepository.delete(report);
        }
        facilityRepository.delete(facility);
    }

    // Create new facilities
    if (request.getBeds() > 0) {
        Facility bed = new Facility();
        bed.setHouse(house);
        bed.setType("bed");
        bed.setQuantity(request.getBeds());
        facilityRepository.save(bed);
    }

    if (request.getMattresses() > 0) {
        Facility mattress = new Facility();
        mattress.setHouse(house);
        mattress.setType("mattress");
        mattress.setQuantity(request.getMattresses());
        facilityRepository.save(mattress);
    }

    if (request.getTables() > 0) {
        Facility table = new Facility();
        table.setHouse(house);
        table.setType("table");
        table.setQuantity(request.getTables());
        facilityRepository.save(table);
    }

    if (request.getChairs() > 0) {
        Facility chair = new Facility();
        chair.setHouse(house);
        chair.setType("chair");
        chair.setQuantity(request.getChairs());
        facilityRepository.save(chair);
    }

    return getHouseFacilities(houseId);
}

    private String getEmployeeName(Integer employeeId) {
        try {
            // TODO: Implement EmployeeClient call to get employee name
            return "Employee " + employeeId; // Placeholder
        } catch (Exception e) {
            return "Unknown Employee";
        }
    }
}