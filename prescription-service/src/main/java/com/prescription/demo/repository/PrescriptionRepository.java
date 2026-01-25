package com.prescription.demo.repository;

import com.prescription.demo.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PrescriptionRepository extends JpaRepository<Prescription, UUID> {
}
