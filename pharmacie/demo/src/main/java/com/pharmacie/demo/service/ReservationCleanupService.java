package com.pharmacie.demo.service;

import com.pharmacie.demo.model.Reservation;
import com.pharmacie.demo.repository.ReservationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.stream.Collectors;
import com.pharmacie.demo.repository.PrescriptionRepository;
import com.pharmacie.demo.model.Prescription;

@Service
public class ReservationCleanupService {
        @Autowired
        private PrescriptionRepository prescriptionRepository;
    @Autowired
    private ReservationRepository reservationRepository;


    @Scheduled(fixedRate = 3600000) // every hour
    public void cleanupReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        List<String> validReservationIds = prescriptionRepository.findAll().stream()
            .map(Prescription::getPrescriptionId)
            .collect(Collectors.toList());

        List<Reservation> toDelete = reservations.stream()
            .filter(r -> !validReservationIds.contains(r.getReservationId()))
            .collect(Collectors.toList());
        toDelete.forEach(r -> reservationRepository.deleteById(r.getReservationId()));
    }
}
