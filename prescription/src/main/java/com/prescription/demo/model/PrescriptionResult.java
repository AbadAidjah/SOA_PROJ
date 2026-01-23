package main.java.com.prescription.demo.model;

public class PrescriptionResult {

    private String status;
    private String message;
    private String reservationId;

    public PrescriptionResult(String status, String message, String reservationId) {
        this.status = status;
        this.message = message;
        this.reservationId = reservationId;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getReservationId() {
        return reservationId;
    }
}