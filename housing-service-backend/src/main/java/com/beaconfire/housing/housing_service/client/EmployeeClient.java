package com.beaconfire.housing.housing_service.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.beaconfire.housing.housing_service.dto.response.EmployeeResponse;

import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;

@FeignClient(
    name = "employee-service",
    url = "${employee.service.url}",
    configuration = EmployeeClient.FeignClientConfig.class
)
public interface EmployeeClient {
    
    @GetMapping("/employees/house/{houseId}")
    List<EmployeeResponse> getEmployeesByHouseId(@PathVariable("houseId") String houseId);
    
    @GetMapping("/employees/{employeeId}/name")
    String getEmployeeFullName(@PathVariable("employeeId") String employeeId);
    
    @GetMapping("/employees/house/{houseId}/count")
    Integer getResidentCountByHouseId(@PathVariable("houseId") String houseId);

    @Configuration
    class FeignClientConfig {
        @Bean
        public RequestInterceptor requestInterceptor() {
            return requestTemplate -> {
                // For now using basic auth with admin:admin
                requestTemplate.header("Authorization", "Basic YWRtaW46YWRtaW4=");
            };
        }

        @Bean
        public ErrorDecoder errorDecoder() {
            return (methodKey, response) -> {
                int status = response.status();
                switch (status) {
                    case 400:
                        return new RuntimeException("Bad request to employee service");
                    case 401:
                        return new RuntimeException("Unauthorized access to employee service");
                    case 403:
                        return new RuntimeException("Forbidden access to employee service");
                    default:
                        return new RuntimeException("Employee service error: " + status);
                }
            };
        }
    }
}