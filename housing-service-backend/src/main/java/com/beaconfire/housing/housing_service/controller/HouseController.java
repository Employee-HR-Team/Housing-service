package com.beaconfire.housing.housing_service.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.beaconfire.housing.housing_service.dto.request.AddHouseRequest;
import com.beaconfire.housing.housing_service.dto.request.UpdateHouseFacilitiesRequest;
import com.beaconfire.housing.housing_service.dto.response.FacilityResponse;
import com.beaconfire.housing.housing_service.dto.response.HouseResponse;
import com.beaconfire.housing.housing_service.dto.response.HouseSummaryResponse;
import com.beaconfire.housing.housing_service.service.HouseService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/houses")
@RequiredArgsConstructor
@Api(tags = "House Management")
public class HouseController {

    private final HouseService houseService;

    @PostMapping
    @ApiOperation("Add a new house")
    public ResponseEntity<HouseResponse> addHouse(@Valid @RequestBody AddHouseRequest request) {
        return ResponseEntity.ok(houseService.addHouse(request));
    }

    @GetMapping
    @ApiOperation("Get all houses summary")
    public ResponseEntity<List<HouseSummaryResponse>> getAllHouses() {
        return ResponseEntity.ok(houseService.getAllHouseSummaries());
    }

    @GetMapping("/{id}")
    @ApiOperation("Get house details by ID")
    public ResponseEntity<HouseResponse> getHouse(@PathVariable Integer id) {
        return ResponseEntity.ok(houseService.getHouseResponse(id));
    }

    @GetMapping("/{id}/summary")
    @ApiOperation("Get house summary by ID")
    public ResponseEntity<HouseSummaryResponse> getHouseSummary(@PathVariable Integer id) {
        return ResponseEntity.ok(houseService.getHouseSummary(id));
    }

    @GetMapping("/{houseId}/facilities")
    @ApiOperation("Get facility information for a house")
    public ResponseEntity<List<FacilityResponse>> getHouseFacilities(@PathVariable Integer houseId) {
        return ResponseEntity.ok(houseService.getHouseFacilities(houseId));
    }

    @PutMapping("/{houseId}/facilities")
    @ApiOperation("Update facility information")
    public ResponseEntity<List<FacilityResponse>> updateHouseFacilities(
            @PathVariable Integer houseId,
            @Valid @RequestBody UpdateHouseFacilitiesRequest request) {
        return ResponseEntity.ok(houseService.updateHouseFacilities(houseId, request));
    }  

    @PutMapping("/{id}")
    @ApiOperation("Update house details")
    public ResponseEntity<HouseResponse> updateHouse(
            @PathVariable Integer id,
            @Valid @RequestBody AddHouseRequest request) {
        return ResponseEntity.ok(houseService.updateHouse(id, request));
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete a house")
    public ResponseEntity<Void> deleteHouse(@PathVariable Integer id) {
        houseService.deleteHouse(id);
        return ResponseEntity.noContent().build();
    }
}
