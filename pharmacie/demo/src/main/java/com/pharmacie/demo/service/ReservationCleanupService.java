
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
import com.pharmacie.demo.repository.ReservationLineRepository;
import com.pharmacie.demo.repository.StockItemRepository;
import com.pharmacie.demo.model.ReservationLine;
import com.pharmacie.demo.model.StockItem;

@Service
public class ReservationCleanupService {
        @Autowired
        private PrescriptionRepository prescriptionRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired 
    private ReservationLineRepository reservationLineRepository;
     @Autowired
    private StockItemRepository stockItemRepository;


    @Scheduled(fixedRate = 3600000) 
    public void cleanupReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        List<String> validReservationIds = prescriptionRepository.findAll().stream()
            .map(Prescription::getPrescriptionId)
            .collect(Collectors.toList());

        List<Reservation> toDelete = reservations.stream()
            .filter(r -> !validReservationIds.contains(r.getReservationId()))
            .collect(Collectors.toList());
        for (Reservation reservation : toDelete) {
        
            List<ReservationLine> lines = reservationLineRepository.findAll().stream()
                .filter(line -> reservation.getReservationId().equals(
                    line.getReservation() != null ? line.getReservation().getReservationId() : null))
                .collect(Collectors.toList());
            for (ReservationLine line : lines) {
                stockItemRepository.findByDrugCode(line.getDrugCode()).ifPresent(stock -> {
                    stock.setQuantityAvailable(stock.getQuantityAvailable() + line.getQtyReserved());
                    stockItemRepository.save(stock);
                });
                reservationLineRepository.delete(line);
            }
            reservationRepository.deleteById(reservation.getReservationId());
        }
    }
}
