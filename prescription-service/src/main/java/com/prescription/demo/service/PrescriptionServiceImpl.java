package com.prescription.demo.service;

import com.prescription.demo.model.Prescription;
import com.prescription.demo.model.PrescriptionStatus;
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
        // Prepare drugs list for validation service
        var drugs = prescription.getItems().stream().map(item -> {
            Map<String, Object> map = new HashMap<>();
            map.put("name", item.getDrugCode());
            map.put("quantity", item.getQuantity());
            return map;
        }).collect(Collectors.toList());

        ValidationServiceClient.ValidationResult validationResult = validationServiceClient.validatePrescription(drugs);
        if (validationResult != null && validationResult.isOk()) {
            prescription.setStatus(PrescriptionStatus.PENDING_VALIDATION);
            prescription.setReservationId(validationResult.getReservationId());
        } else {
            prescription.setStatus(PrescriptionStatus.REJECTED);
        }
        return prescriptionRepository.save(prescription);
    }

    @Override
    public Optional<Prescription> getPrescription(UUID id) {
        return prescriptionRepository.findById(id);
    }

    @Override
    public List<Prescription> getAllPrescriptions() {
        return prescriptionRepository.findAll();
    }

    @Override
    public Prescription updateStatus(UUID id, PrescriptionStatus status) {
        Optional<Prescription> opt = prescriptionRepository.findById(id);
        if (opt.isPresent()) {
            Prescription p = opt.get();
            p.setStatus(status);
            return prescriptionRepository.save(p);
        }
        throw new NoSuchElementException("Prescription not found");
    }

    @Override
    public void deletePrescription(UUID id) {
        prescriptionRepository.deleteById(id);
    }
}
