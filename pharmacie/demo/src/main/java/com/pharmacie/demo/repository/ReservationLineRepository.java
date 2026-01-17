package com.pharmacie.demo.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pharmacie.demo.model.ReservationLine;

public interface ReservationLineRepository extends JpaRepository<ReservationLine, UUID> {
    
}
