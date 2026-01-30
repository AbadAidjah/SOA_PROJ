package com.pharmacie.demo.repository;

import com.pharmacie.demo.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionRepository extends JpaRepository<Prescription, String> {
}
