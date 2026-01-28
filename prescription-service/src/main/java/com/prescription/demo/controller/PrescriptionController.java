package com.prescription.demo.controller;

import com.prescription.demo.model.Prescription;
// import com.prescription.demo.model.PrescriptionStatus;
import com.prescription.demo.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @PostMapping
    public ResponseEntity<Prescription> createPrescription(@RequestBody Prescription prescription) {
        Prescription created = prescriptionService.createPrescription(prescription);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prescription> getPrescription(@PathVariable UUID id) {
        Optional<Prescription> prescription = prescriptionService.getPrescription(id);
        return prescription.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Prescription> getAllPrescriptions() {
        return prescriptionService.getAllPrescriptions();
    }

    // @PutMapping("/{id}/status")
    // public ResponseEntity<Prescription> updateStatus(@PathVariable UUID id, @RequestBody PrescriptionStatus status) {
    //     Prescription updated = prescriptionService.updateStatus(id, status);
    //     return ResponseEntity.ok(updated);
    // }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrescription(@PathVariable UUID id) {
        prescriptionService.deletePrescription(id);
        return ResponseEntity.noContent().build();
    }
}
