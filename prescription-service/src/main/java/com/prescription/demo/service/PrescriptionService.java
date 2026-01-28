package com.prescription.demo.service;

import com.prescription.demo.model.Prescription;
// import removed: PrescriptionStatus
import java.util.*;

public interface PrescriptionService {
    Prescription createPrescription(Prescription prescription);
    Optional<Prescription> getPrescription(UUID id);
    List<Prescription> getAllPrescriptions();
    // updateStatus method removed
    void deletePrescription(UUID id);
}
