package com.beaconfire.housing.housing_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "employee-service")
public interface EmployeeClient {
    @GetMapping("/api/employees/{id}/name")
    String getEmployeeName(@PathVariable("id") Integer employeeId);

    @GetMapping("/api/employees/house/{houseId}/count")
    Integer getResidentCountByHouseId(@PathVariable("houseId") Integer houseId);
}