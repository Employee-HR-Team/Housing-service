package com.beaconfire.housing.housing_service.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.beaconfire.housing.housing_service.client.EmployeeClient;
import com.beaconfire.housing.housing_service.dto.response.EmployeeResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {
    private static final Logger log = LoggerFactory.getLogger(TestController.class);
    
    private final EmployeeClient employeeClient;
    
    @GetMapping("/house/{houseId}/employees")
    public ResponseEntity<List<EmployeeResponse>> testEmployeesByHouse(@PathVariable String houseId) {
        log.info("Testing get employees by house ID: {}", houseId);
        List<EmployeeResponse> employees = employeeClient.getEmployeesByHouseId(houseId);
        return ResponseEntity.ok(employees);
    }
    
    @GetMapping("/employee/{employeeId}/name")
    public ResponseEntity<String> testEmployeeName(@PathVariable String employeeId) {
        log.info("Testing get employee name by ID: {}", employeeId);
        String name = employeeClient.getEmployeeFullName(employeeId);
        return ResponseEntity.ok(name);
    }
    
    @GetMapping("/house/{houseId}/count")
    public ResponseEntity<Integer> testResidentCount(@PathVariable String houseId) {
        log.info("Testing get resident count by house ID: {}", houseId);
        Integer count = employeeClient.getResidentCountByHouseId(houseId);
        return ResponseEntity.ok(count);
    }
}