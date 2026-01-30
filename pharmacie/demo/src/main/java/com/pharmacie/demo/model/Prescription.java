package com.pharmacie.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "prescriptions") // adjust if your table name is different
public class Prescription {
    @Id
    private String prescriptionId;

    // Add other fields if needed

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
    }
}
