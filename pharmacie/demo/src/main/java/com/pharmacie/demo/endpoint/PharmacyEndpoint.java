package com.pharmacie.demo.endpoint;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.pharmacie.demo.service.StockItemService;
import com.pharmacie.demo.service.ReservationService;
import com.pharmacie.demo.service.ReservationLineService;

// Import your generated JAXB classes here
import com.pharmacie.demo.GetStockRequest;
import com.pharmacie.demo.GetStockResponse;
import com.pharmacie.demo.ReserveMedicinesRequest;
import com.pharmacie.demo.ReserveMedicinesResponse;
import com.pharmacie.demo.model.Reservation;
import com.pharmacie.demo.model.ReservationLine;
import com.pharmacie.demo.CancelReservationRequest;
import com.pharmacie.demo.CancelReservationResponse;
import com.pharmacie.demo.DispenseReservationRequest;
import com.pharmacie.demo.DispenseReservationResponse;

@Endpoint
public class PharmacyEndpoint {
    private static final String NAMESPACE_URI = "http://pharmacie.com/demo";

    @Autowired
    private StockItemService stockItemService;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ReservationLineService reservationLineService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getStockRequest")
    @ResponsePayload
    public GetStockResponse getStock(@RequestPayload GetStockRequest request) {
        GetStockResponse response = new GetStockResponse();
        System.out.println("hhhhhhhhhhh"+ "" +request.getDrugCode());
        System.out.println("Request class: " + request.getClass().getName());
        Integer qty = stockItemService.getQuantityAvailableByDrugCode(request.getDrugCode());
        response.setQuantityAvailable(qty == null ? 0: qty);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "reserveMedicinesRequest")
    @ResponsePayload
    public ReserveMedicinesResponse reserveMedicines(@RequestPayload ReserveMedicinesRequest request) {
        ReserveMedicinesResponse response = new ReserveMedicinesResponse();
        Reservation reservation = new Reservation();
        reservation.setReservationId(UUID.randomUUID().toString());
        // reservation.setStatus(Reservation.ReservationStatus.);
         reservationService.saveReservation(reservation);
        for(ReserveMedicinesRequest.Items item: request.getItems()){

            Integer available = stockItemService.getQuantityAvailableByDrugCode(item.getDrugCode());
            if(available == null || available < item.getQtyReserved()){
            response.setStatus("FAILED");
            response.setMessage("aucun quantiter sufisante de ce drug " + item.getDrugCode());
            return response;
            }
            ReservationLine line = new ReservationLine();
            line.setDrugCode(item.getDrugCode());
            line.setQtyReserved(item.getQtyReserved());
            line.setReservation(reservation);
            reservationLineService.saveReservationLine(line);

        }
        reservation.setStatus(Reservation.ReservationStatus.RESERVED);
        reservationService.saveReservation(reservation);
       response.setReservationId(reservation.getReservationId().toString());
       response.setStatus("SUCCESS");
       response.setMessage("Reservation a ete creer ");
        return response;
    }

    // @PayloadRoot(namespace = NAMESPACE_URI, localPart = "cancelReservationRequest")
    // @ResponsePayload
    // public CancelReservationResponse cancelReservation(@RequestPayload CancelReservationRequest request) {
    //     CancelReservationResponse response = new CancelReservationResponse();
        
    //     return response;
    // }

    // @PayloadRoot(namespace = NAMESPACE_URI, localPart = "dispenseReservationRequest")
    // @ResponsePayload
    // public DispenseReservationResponse dispenseReservation(@RequestPayload DispenseReservationRequest request) {
    //     DispenseReservationResponse response = new DispenseReservationResponse();
       
    //     return response;
    // }
}
