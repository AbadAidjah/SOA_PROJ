package com.prescription.demo.service;

import com.prescription.demo.model.Prescription;
import com.prescription.demo.model.PrescriptionStatus;
import java.util.*;

public interface PrescriptionService {
    Prescription createPrescription(Prescription prescription);
    Optional<Prescription> getPrescription(UUID id);
    List<Prescription> getAllPrescriptions();
    Prescription updateStatus(UUID id, PrescriptionStatus status);
    void deletePrescription(UUID id);
}
