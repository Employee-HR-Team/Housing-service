package com.beaconfire.housing.housing_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.beaconfire.housing.housing_service.dto.response.LandlordResponse;
import com.beaconfire.housing.housing_service.service.LandlordService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/landlords")
@RequiredArgsConstructor
@Api(tags = "Landlord Management")
public class LandlordController {
    private final LandlordService landlordService;

    @GetMapping
    @ApiOperation("Get all landlords")
    public ResponseEntity<List<LandlordResponse>> getAllLandlords() {
        return ResponseEntity.ok(landlordService.getAllLandlords());
    }

    @GetMapping("/{id}")
    @ApiOperation("Get landlord by ID")
    public ResponseEntity<LandlordResponse> getLandlord(@PathVariable Integer id) {
        return ResponseEntity.ok(landlordService.getLandlordById(id));
    }

    @GetMapping("/search")
    @ApiOperation("Search landlords")
    public ResponseEntity<List<LandlordResponse>> searchLandlords(@RequestParam String searchTerm) {
        return ResponseEntity.ok(landlordService.searchLandlords(searchTerm));
    }

    @PostMapping
    @ApiOperation("Create new landlord")
    public ResponseEntity<LandlordResponse> createLandlord(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String phone) {
        return ResponseEntity.ok(landlordService.createLandlord(firstName, lastName, email, phone));
    }

    @PutMapping("/{id}")
    @ApiOperation("Update landlord details")
    public ResponseEntity<LandlordResponse> updateLandlord(
            @PathVariable Integer id,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone) {
        return ResponseEntity.ok(landlordService.updateLandlordDetails(id, firstName, lastName, email, phone));
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete landlord")
    public ResponseEntity<Void> deleteLandlord(@PathVariable Integer id) {
        landlordService.deleteLandlord(id);
        return ResponseEntity.noContent().build();
    }
}