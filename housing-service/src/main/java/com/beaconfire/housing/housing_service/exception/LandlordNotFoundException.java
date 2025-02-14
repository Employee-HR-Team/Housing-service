package com.beaconfire.housing.housing_service.exception;

public class LandlordNotFoundException extends RuntimeException {
    public LandlordNotFoundException(String message) {
        super(message);
    }
}