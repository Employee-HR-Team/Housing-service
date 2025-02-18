package com.beaconfire.housing.housing_service.exception;

public class FacilityNotFoundException extends RuntimeException {
    public FacilityNotFoundException(String message) {
        super(message);
    }
}