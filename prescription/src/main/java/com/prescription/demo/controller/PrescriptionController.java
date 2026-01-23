package com.prescription.demo.controller;

import com.prescription.demo.model.PrescriptionRequest;
import com.prescription.demo.model.PrescriptionResult;
import com.prescription.demo.service.PrescriptionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour la gestion des prescriptions
 */
@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    private static final Logger logger = LoggerFactory.getLogger(PrescriptionController.class);

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    /**
     * Crée et traite une nouvelle prescription
     * 
     * @param request La demande de prescription
     * @return Le résultat du traitement
     */
    @PostMapping
    public ResponseEntity<PrescriptionResult> create(@Valid @RequestBody PrescriptionRequest request) {
        logger.info("Reçu une nouvelle demande de prescription ID: {}", request.getPrescriptionId());
        PrescriptionResult result = prescriptionService.process(request);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}