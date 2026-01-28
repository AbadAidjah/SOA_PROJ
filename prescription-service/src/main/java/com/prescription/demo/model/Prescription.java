
package com.prescription.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
// import javax.persistence.*;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Prescription {
    @Id
    @GeneratedValue
    private UUID id;

    // @Enumerated(EnumType.STRING)
    // private PrescriptionStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime submittedAt;
    private String patientName;
    private String doctorName;
    private String reservationId; // optional

    @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PrescriptionItem> items;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    // public PrescriptionStatus getStatus() { return status; }
    // public void setStatus(PrescriptionStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    public String getReservationId() { return reservationId; }
    public void setReservationId(String reservationId) { this.reservationId = reservationId; }
    public List<PrescriptionItem> getItems() { return items; }
    public void setItems(List<PrescriptionItem> items) { this.items = items; }
}
