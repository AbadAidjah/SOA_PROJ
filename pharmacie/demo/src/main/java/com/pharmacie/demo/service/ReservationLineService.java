package com.pharmacie.demo.service;


import com.pharmacie.demo.model.ReservationLine;
import com.pharmacie.demo.repository.ReservationLineRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReservationLineService {
    private final ReservationLineRepository reservationLineRepository;

    public ReservationLineService(ReservationLineRepository reservationLineRepository) {
        this.reservationLineRepository = reservationLineRepository;
    }

    public List<ReservationLine> getAllReservationLines() {
        return reservationLineRepository.findAll();
    }

    public Optional<ReservationLine> getReservationLineById(UUID id) {
        return reservationLineRepository.findById(id);
    }

    public ReservationLine saveReservationLine(ReservationLine reservationLine) {
        return reservationLineRepository.save(reservationLine);
    }

    public void deleteReservationLine(UUID id) {
        reservationLineRepository.deleteById(id);
    }
}
