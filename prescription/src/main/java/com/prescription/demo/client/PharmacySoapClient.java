package com.prescription.demo.client;

import com.pharmacie.demo.ReserveMedicinesRequest;
import com.pharmacie.demo.ReserveMedicinesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.core.WebServiceTemplate;

/**
 * Client SOAP pour communiquer avec le service pharmacie
 */
@Component
public class PharmacySoapClient {

    private static final Logger logger = LoggerFactory.getLogger(PharmacySoapClient.class);

    private final WebServiceTemplate webServiceTemplate;

    public PharmacySoapClient(WebServiceTemplate webServiceTemplate) {
        this.webServiceTemplate = webServiceTemplate;
    }

    /**
     * Réserve des médicaments auprès du service pharmacie via SOAP
     * 
     * @param request La demande de réservation
     * @return La réponse de réservation
     * @throws WebServiceClientException Si l'appel SOAP échoue
     */
    public ReserveMedicinesResponse reserve(ReserveMedicinesRequest request) {
        try {
            logger.debug("Envoi de la requête SOAP de réservation pour prescription ID: {}", 
                request.getPrescriptionId());
            logger.debug("Nombre de médicaments à réserver: {}", 
                request.getItems() != null ? request.getItems().size() : 0);

            ReserveMedicinesResponse response = (ReserveMedicinesResponse)
                webServiceTemplate.marshalSendAndReceive(request);

            if (response != null) {
                logger.debug("Réponse SOAP reçue. Status: {}, Reservation ID: {}", 
                    response.getStatus(), response.getReservationId());
            } else {
                logger.error("Réponse SOAP nulle");
            }

            return response;

        } catch (WebServiceClientException e) {
            logger.error("Erreur lors de l'appel SOAP au service pharmacie: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de l'appel SOAP: {}", e.getMessage(), e);
            throw new WebServiceClientException("Erreur lors de la communication SOAP", e);
        }
    }
}