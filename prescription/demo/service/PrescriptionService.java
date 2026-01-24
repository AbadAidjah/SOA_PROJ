package com.prescription.demo.service;

import com.prescription.demo.client.PharmacySoapClient;
import com.prescription.demo.client.ValidationClient;
import com.prescription.demo.exception.ValidationException;
import com.prescription.demo.exception.PharmacyServiceException;
import com.prescription.demo.model.*;

import com.pharmacie.demo.ReserveMedicinesRequest;
import com.pharmacie.demo.ReserveMedicinesRequest.Items;
import com.pharmacie.demo.ReserveMedicinesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service principal pour le traitement des prescriptions
 * Orchestre la validation et la réservation des médicaments
 */
@Service
public class PrescriptionService {

    private static final Logger logger = LoggerFactory.getLogger(PrescriptionService.class);

    private final ValidationClient validationClient;
    private final PharmacySoapClient pharmacySoapClient;

    public PrescriptionService(ValidationClient validationClient, PharmacySoapClient pharmacySoapClient) {
        this.validationClient = validationClient;
        this.pharmacySoapClient = pharmacySoapClient;
    }

    /**
     * Traite une prescription complète :
     * 1. Valide les interactions médicamenteuses
     * 2. Réserve les médicaments dans la pharmacie
     * 
     * @param request La demande de prescription
     * @return Le résultat du traitement
     * @throws ValidationException Si des interactions sont détectées
     * @throws PharmacyServiceException Si la réservation échoue
     */
    public PrescriptionResult process(PrescriptionRequest request) {
        logger.info("Traitement de la prescription ID: {}", request.getPrescriptionId());
        
        // Validation des entrées
        validateRequest(request);

        try {
            // 1. Validation des interactions médicamenteuses
            logger.debug("Appel du service de validation pour prescription ID: {}", request.getPrescriptionId());
            ValidationResponse validation = validationClient.validate(request);
            
            if (!validation.isOk()) {
                logger.warn("Interactions médicamenteuses détectées pour prescription ID: {}", request.getPrescriptionId());
                String issuesMessage = buildIssuesMessage(validation.getIssues());
                throw new ValidationException(
                    "Interaction médicamenteuse détectée",
                    validation.getIssues()
                );
            }

            logger.info("Validation réussie pour prescription ID: {}", request.getPrescriptionId());

            // 2. Construction de la requête SOAP
            ReserveMedicinesRequest soapRequest = buildSoapRequest(request, validation);
            logger.debug("Requête SOAP construite avec {} médicaments", soapRequest.getItems().size());

            // 3. Appel du service pharmacie
            logger.debug("Appel du service pharmacie pour prescription ID: {}", request.getPrescriptionId());
            ReserveMedicinesResponse response = pharmacySoapClient.reserve(soapRequest);

            if (response == null) {
                logger.error("Réponse nulle du service pharmacie pour prescription ID: {}", request.getPrescriptionId());
                throw new PharmacyServiceException("Erreur lors de la communication avec le service pharmacie");
            }

            if (!"SUCCESS".equalsIgnoreCase(response.getStatus())) {
                logger.warn("Échec de la réservation pour prescription ID: {}. Message: {}", 
                    request.getPrescriptionId(), response.getMessage());
                throw new PharmacyServiceException(
                    response.getMessage() != null ? response.getMessage() : "Échec de la réservation"
                );
            }

            logger.info("Prescription traitée avec succès. Reservation ID: {}", response.getReservationId());
            
            return new PrescriptionResult(
                response.getStatus(),
                response.getMessage(),
                response.getReservationId()
            );

        } catch (ValidationException | PharmacyServiceException e) {
            logger.error("Erreur lors du traitement de la prescription ID: {}", request.getPrescriptionId(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Erreur inattendue lors du traitement de la prescription ID: {}", 
                request.getPrescriptionId(), e);
            throw new PharmacyServiceException("Erreur interne lors du traitement de la prescription", e);
        }
    }

    /**
     * Valide la requête de prescription
     */
    private void validateRequest(PrescriptionRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La requête de prescription ne peut pas être nulle");
        }
        if (request.getPrescriptionId() == null || request.getPrescriptionId().trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID de prescription est requis");
        }
        if (request.getDrugs() == null || request.getDrugs().isEmpty()) {
            throw new IllegalArgumentException("Au moins un médicament est requis");
        }
        for (PrescriptionRequest.DrugItem drug : request.getDrugs()) {
            if (drug.getName() == null || drug.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("Le nom du médicament est requis");
            }
            if (drug.getQuantity() <= 0) {
                throw new IllegalArgumentException("La quantité doit être supérieure à 0");
            }
        }
    }

    /**
     * Construit la requête SOAP à partir de la prescription et de la validation
     */
    private ReserveMedicinesRequest buildSoapRequest(PrescriptionRequest request, ValidationResponse validation) {
        ReserveMedicinesRequest soapRequest = new ReserveMedicinesRequest();
        soapRequest.setPrescriptionId(request.getPrescriptionId());
        
        List<Items> items = new ArrayList<>();
        for (ValidationResponse.MappedItem item : validation.getMappedItems()) {
            Items soapItem = new Items();
            soapItem.setDrugCode(item.drugId);
            soapItem.setQtyReserved(item.quantity);
            items.add(soapItem);
        }
        soapRequest.getItems().addAll(items);
        
        return soapRequest;
    }

    /**
     * Construit un message détaillé des interactions détectées
     */
    private String buildIssuesMessage(List<ValidationResponse.Issue> issues) {
        if (issues == null || issues.isEmpty()) {
            return "Interaction médicamenteuse détectée";
        }
        StringBuilder message = new StringBuilder("Interactions détectées: ");
        for (ValidationResponse.Issue issue : issues) {
            message.append(String.format("%s-%s (%s); ", 
                issue.drugA, issue.drugB, issue.level));
        }
        return message.toString();
    }
}