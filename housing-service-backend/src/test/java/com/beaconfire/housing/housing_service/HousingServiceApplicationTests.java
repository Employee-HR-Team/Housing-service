package com.beaconfire.housing.housing_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import com.beaconfire.housing.housing_service.client.EmployeeClient;

@SpringBootTest
@TestPropertySource(properties = {
    "aws.access.key.id=test",
    "aws.secret.access.key=test",
    "aws.s3.region=us-east-1"
})
class HousingServiceApplicationTests {
    @MockBean
    private EmployeeClient employeeClient;

    @Test
    void contextLoads() {
    }
}