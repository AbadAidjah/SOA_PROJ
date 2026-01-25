package com.prescription.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalServiceClient {
    private final RestTemplate restTemplate = new RestTemplate();

    public boolean validatePrescription(Object prescription) {
        // Call validation-service
        // Example: restTemplate.postForObject("http://localhost:8082/api/validate", prescription, Boolean.class);
        return true;
    }

    public boolean reserveDrugs(Object prescription) {
        // Call pharmacy-service
        // Example: restTemplate.postForObject("http://localhost:8083/api/reserve", prescription, Boolean.class);
        return true;
    }
}
