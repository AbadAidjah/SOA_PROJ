package com.pharmacie.demo.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ReservationLine {
    @Id
    private UUID id;
    // private String reservationId;
    private String drugCode;
    private int qtyReserved;

    @ManyToOne
    @JoinColumn(name = "reservation_id", referencedColumnName = "reservationId", insertable = false, updatable = false)
    private Reservation reservation;

    public ReservationLine() {
        this.id = UUID.randomUUID();
    }

   

public UUID getId() {
    return id;
}

public void setId(UUID id) {
    this.id = id;
}

// public String getReservationId() {
//     return reservationId;
// }

// public void setReservationId(String reservationId) {
//     this.reservationId = reservationId;
// }

public String getDrugCode() {
    return drugCode;
}

public void setDrugCode(String drugCode) {
    this.drugCode = drugCode;
}

public int getQtyReserved() {
    return qtyReserved;
}

public void setQtyReserved(int qtyReserved) {
    this.qtyReserved = qtyReserved;
}

public Reservation getReservation() {
    return reservation;
}

public void setReservation(Reservation reservation) {
    this.reservation = reservation;
}
}

