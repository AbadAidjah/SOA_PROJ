package main.java.com.prescription.demo.service;

import com.prescription.demo.client.PharmacySoapClient;
import com.prescription.demo.client.ValidationClient;
import com.prescription.demo.model.*;

import com.pharmacie.demo.ReserveMedicinesRequest;
import com.pharmacie.demo.ReserveMedicinesRequest.Items;
import com.pharmacie.demo.ReserveMedicinesResponse;
import org.springframework.stereotype.Service;

@Service
public class PrescriptionService {

    private final ValidationClient validationClient;
    private final PharmacySoapClient pharmacySoapClient;

    public PrescriptionService(ValidationClient validationClient, PharmacySoapClient pharmacySoapClient) {
    this.validationClient = validationClient;
    this.pharmacySoapClient = pharmacySoapClient;
}


    public PrescriptionResult process(PrescriptionRequest request) {

        // 1. Validation
        ValidationResponse validation = validationClient.validate(request);
        if (!validation.isOk()) {
            return new PrescriptionResult(
                    "FAILED",
                    "Interaction médicamenteuse détectée",
                    null
            );
        }

        // 2. Build SOAP request
        ReserveMedicinesRequest soapRequest = new ReserveMedicinesRequest();
        soapRequest.setPrescriptionId(request.getPrescriptionId());

        for (ValidationResponse.MappedItem item : validation.getMappedItems()) {
            Items i = new Items();
            i.setDrugCode(item.drugId);
            i.setQtyReserved(item.quantity);
            soapRequest.getItems().add(i);
        }

        // 3. Call pharmacie
        ReserveMedicinesResponse response =
                pharmacySoapClient.reserve(soapRequest);

        return new PrescriptionResult(
                response.getStatus(),
                response.getMessage(),
                response.getReservationId()
        );
    }
}