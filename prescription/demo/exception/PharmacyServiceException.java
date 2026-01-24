package com.prescription.demo.exception;

/**
 * Exception levée lorsqu'une erreur survient lors de la communication avec le service pharmacie
 */
public class PharmacyServiceException extends RuntimeException {

    public PharmacyServiceException(String message) {
        super(message);
    }

    public PharmacyServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
