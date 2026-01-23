package main.java.com.prescription.demo.client;

import com.pharmacie.demo.ReserveMedicinesRequest;
import com.pharmacie.demo.ReserveMedicinesResponse;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

@Component
public class PharmacySoapClient {

    private final WebServiceTemplate webServiceTemplate;

    public PharmacySoapClient(WebServiceTemplate webServiceTemplate) {
        this.webServiceTemplate = webServiceTemplate;
    }

    public ReserveMedicinesResponse reserve(ReserveMedicinesRequest request) {
        return (ReserveMedicinesResponse)
                webServiceTemplate.marshalSendAndReceive(request);
    }
}