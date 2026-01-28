package com.prescription.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ValidationServiceClient {
    @Value("${validation-service.url}")
    private String validationServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public ValidationResult validatePrescription(List<Map<String, Object>> drugs) {
        Map<String, Object> request = new HashMap<>();
        request.put("drugs", drugs);
        ResponseEntity<ValidationResult> response = restTemplate.postForEntity(validationServiceUrl, request, ValidationResult.class);
        return response.getBody();
    }

    // DTO for deserialization
    public static class ValidationResult {
        private boolean ok;
        private String reservationId;
        private java.util.List<java.util.Map<String, Object>> issues;

        public boolean isOk() { return ok; }
        public void setOk(boolean ok) { this.ok = ok; }

        public String getReservationId() { return reservationId; }
        public void setReservationId(String reservationId) { this.reservationId = reservationId; }

        public java.util.List<java.util.Map<String, Object>> getIssues() { return issues; }
        public void setIssues(java.util.List<java.util.Map<String, Object>> issues) { this.issues = issues; }
    }
}
