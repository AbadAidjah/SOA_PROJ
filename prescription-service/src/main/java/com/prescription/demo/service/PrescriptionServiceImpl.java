package com.prescription.demo.service;

import com.prescription.demo.model.Prescription;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
// import removed: PrescriptionStatus
import com.prescription.demo.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import java.util.*;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private ValidationServiceClient validationServiceClient;

    @Override
    public Prescription createPrescription(Prescription prescription) {
        prescription.setCreatedAt(java.time.LocalDateTime.now());
        prescription.setSubmittedAt(java.time.LocalDateTime.now());

        // Ensure each item is linked to its parent prescription for JPA
        if (prescription.getItems() != null) {
            for (var item : prescription.getItems()) {
                item.setPrescription(prescription);
            }
        }

        // Prepare drugs list for validation service
        List<Map<String, Object>> drugs = prescription.getItems() == null ? new ArrayList<>() :
            prescription.getItems().stream().map(item -> {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("name", item.getDrugCode());
                map.put("quantity", item.getQuantity());
                return map;
            }).collect(Collectors.toList());

        ValidationServiceClient.ValidationResult validationResult = validationServiceClient.validatePrescription(drugs);
        if (validationResult != null && validationResult.getReservationId() != null) {
            prescription.setReservationId(validationResult.getReservationId());
            return prescriptionRepository.save(prescription);
        } else {
            String reason = "Reservation could not be created. Possible drug interaction or validation failure.";
            if (validationResult != null && validationResult.getIssues() != null && !validationResult.getIssues().isEmpty()) {
                reason += " Issues: " + validationResult.getIssues().toString();
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, reason);
        }
    }

    @Override
    public Optional<Prescription> getPrescription(UUID id) {
        return prescriptionRepository.findById(id);
    }

    @Override
    public List<Prescription> getAllPrescriptions() {
        return prescriptionRepository.findAll();
    }

    // updateStatus method removed

    @Override
    public void deletePrescription(UUID id) {
        prescriptionRepository.deleteById(id);
    }
}
