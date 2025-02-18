package com.beaconfire.housing.housing_service.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.beaconfire.housing.housing_service.dto.response.LandlordResponse;
import com.beaconfire.housing.housing_service.entity.Facility;
import com.beaconfire.housing.housing_service.entity.FacilityReport;
import com.beaconfire.housing.housing_service.entity.House;
import com.beaconfire.housing.housing_service.entity.Landlord;
import com.beaconfire.housing.housing_service.exception.LandlordNotFoundException;
import com.beaconfire.housing.housing_service.repository.FacilityReportRepository;
import com.beaconfire.housing.housing_service.repository.FacilityRepository;
import com.beaconfire.housing.housing_service.repository.HouseRepository;
import com.beaconfire.housing.housing_service.repository.LandlordRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class LandlordService {
    private final LandlordRepository landlordRepository;
    private final HouseRepository houseRepository;
    private final FacilityRepository facilityRepository;
    private final FacilityReportRepository facilityReportRepository;

    @Transactional
    public List<LandlordResponse> getAllLandlords() {
        return landlordRepository.findAll().stream()
                .map(this::buildLandlordResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public LandlordResponse getLandlordById(Integer id) {
        Landlord landlord = landlordRepository.findById(id);
        if (landlord == null) {
            throw new LandlordNotFoundException("Landlord not found with id: " + id);
        }
        return buildLandlordResponse(landlord);
    }

    @Transactional
    public LandlordResponse updateLandlord(Integer id, String email, String phone) {
        Landlord landlord = landlordRepository.findById(id);
        if (landlord == null) {
            throw new LandlordNotFoundException("Landlord not found with id: " + id);
        }
        
        if (email != null && !email.isEmpty()) {
            landlord.setEmail(email);
        }
        if (phone != null && !phone.isEmpty()) {
            landlord.setCellPhone(phone);
        }
        
        landlord = landlordRepository.save(landlord);
        return buildLandlordResponse(landlord);
    }

    @Transactional
    public LandlordResponse createLandlord(String firstName, String lastName, String email, String phone) {
        Landlord existingLandlord = landlordRepository.findByEmail(email);
        if (existingLandlord != null) {
            throw new RuntimeException("Landlord with this email already exists");
        }

        Landlord landlord = new Landlord();
        landlord.setFirstName(firstName);
        landlord.setLastName(lastName);
        landlord.setEmail(email);
        landlord.setCellPhone(phone);

        landlord = landlordRepository.save(landlord);
        return buildLandlordResponse(landlord);
    }

    @Transactional
    public void deleteLandlord(Integer id) {
        Landlord landlord = landlordRepository.findById(id);
        if (landlord == null) {
            throw new LandlordNotFoundException("Landlord not found with id: " + id);
        }

        List<House> houses = houseRepository.findByLandlord(landlord);
        for (House house : houses) {
            List<Facility> facilities = facilityRepository.findByHouse(house);
            for (Facility facility : facilities) {
                List<FacilityReport> reports = facilityReportRepository.findByFacility(facility);
                for (FacilityReport report : reports) {
                    facilityReportRepository.delete(report);
                }
            }
            for (Facility facility : facilities) {
                facilityRepository.delete(facility);
            }
            houseRepository.delete(house);
        }
        landlordRepository.delete(landlord);
    }

    @Transactional
    private LandlordResponse buildLandlordResponse(Landlord landlord) {
        List<House> houses = houseRepository.findByLandlord(landlord);

        List<LandlordResponse.HouseInfo> houseInfos = houses.stream()
                .map(house -> {
                    List<Facility> facilities = facilityRepository.findByHouse(house);
                    long openReports = facilities.stream()
                            .flatMap(facility -> facilityReportRepository.findByFacility(facility).stream())
                            .filter(report -> report.getStatus() == FacilityReport.ReportStatus.Open)
                            .count();

                    return new LandlordResponse.HouseInfo(
                            house.getId(),
                            house.getAddress(),
                            getCurrentResidentCount(house),
                            house.getMaxOccupant(),
                            (int) openReports
                    );
                })
                .collect(Collectors.toList());

        int totalResidents = houseInfos.stream()
                .mapToInt(LandlordResponse.HouseInfo::getCurrentOccupants)
                .sum();

        int totalFacilities = houses.stream()
                .flatMap(house -> facilityRepository.findByHouse(house).stream())
                .mapToInt(Facility::getQuantity)
                .sum();

        int totalOpenReports = houseInfos.stream()
                .mapToInt(LandlordResponse.HouseInfo::getOpenFacilityReports)
                .sum();

        LandlordResponse.Statistics statistics = new LandlordResponse.Statistics(
                houses.size(),
                totalResidents,
                totalFacilities,
                totalOpenReports
        );

        return LandlordResponse.builder()
                .id(landlord.getId())
                .firstName(landlord.getFirstName())
                .lastName(landlord.getLastName())
                .email(landlord.getEmail())
                .cellPhone(landlord.getCellPhone())
                .houses(houseInfos)
                .statistics(statistics)
                .build();
    }

    private Integer getCurrentResidentCount(House house) {
        return 0; // Placeholder for EmployeeClient implementation
    }

    @Transactional
    public List<LandlordResponse> searchLandlords(String searchTerm) {
        List<Landlord> allLandlords = landlordRepository.findAll();
        return allLandlords.stream()
                .filter(landlord -> 
                    landlord.getFirstName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                    landlord.getLastName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                    landlord.getEmail().toLowerCase().contains(searchTerm.toLowerCase())
                )
                .map(this::buildLandlordResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public LandlordResponse updateLandlordDetails(Integer id, String firstName, String lastName, 
                                                String email, String phone) {
        Landlord landlord = landlordRepository.findById(id);
        if (landlord == null) {
            throw new LandlordNotFoundException("Landlord not found with id: " + id);
        }
        
        if (firstName != null && !firstName.isEmpty()) {
            landlord.setFirstName(firstName);
        }
        if (lastName != null && !lastName.isEmpty()) {
            landlord.setLastName(lastName);
        }
        if (email != null && !email.isEmpty()) {
            Landlord existingLandlord = landlordRepository.findByEmail(email);
            if (existingLandlord != null && !existingLandlord.getId().equals(id)) {
                throw new RuntimeException("Email is already in use by another landlord");
            }
            landlord.setEmail(email);
        }
        if (phone != null && !phone.isEmpty()) {
            landlord.setCellPhone(phone);
        }
        
        landlord = landlordRepository.save(landlord);
        return buildLandlordResponse(landlord);
    }
}