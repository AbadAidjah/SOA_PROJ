package com.pharmacie.demo.model;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Entity
@Table(name = "reservations")
public class Reservation {
    
    @Id
    private String reservationId;
    private String prescriptionId;
   
    private LocalDateTime createdAt;
    public enum ReservationStatus {
       RESERVED,
       CANCELLED,
       DISPENSED
    }

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    private List<ReservationLine> reservationLines;

    public Reservation() {}

     public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public ReservationStatus getStatus() {
        return reservationStatus;
    }

    public void setStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<ReservationLine> getReservationLines() {
        return reservationLines;
    }

    public void setReservationLines(List<ReservationLine> reservationLines) {
        this.reservationLines = reservationLines;
    }
}
    
    


