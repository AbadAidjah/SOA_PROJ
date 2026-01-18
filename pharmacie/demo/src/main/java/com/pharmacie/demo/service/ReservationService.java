package com.pharmacie.demo.service;

import org.springframework.stereotype.Service;

import com.pharmacie.demo.model.Reservation;
import com.pharmacie.demo.repository.ReservationRepository;


import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {
    

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Optional<Reservation> getReservationById(String id) {
        return reservationRepository.findById(id);
    }

    public Reservation saveReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public void deleteReservation(String id) {
        reservationRepository.deleteById(id);
    }
}
