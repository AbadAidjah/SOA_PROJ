package com.prescription.demo.exception;

import com.prescription.demo.model.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

import java.util.stream.Collectors;

/**
 * Gestionnaire global des exceptions pour le service prescription
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException e) {
        logger.warn("Exception de validation: {}", e.getMessage());
        ErrorResponse error = new ErrorResponse(
            "VALIDATION_FAILED",
            e.getMessage(),
            e.getIssues()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(PharmacyServiceException.class)
    public ResponseEntity<ErrorResponse> handlePharmacyServiceException(PharmacyServiceException e) {
        logger.error("Exception du service pharmacie: {}", e.getMessage(), e);
        ErrorResponse error = new ErrorResponse(
            "PHARMACY_SERVICE_ERROR",
            e.getMessage(),
            null
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        logger.warn("Argument invalide: {}", e.getMessage());
        ErrorResponse error = new ErrorResponse(
            "INVALID_REQUEST",
            e.getMessage(),
            null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException e) {
        logger.warn("Erreur de validation: {}", e.getMessage());
        String message = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining(", "));
        
        ErrorResponse error = new ErrorResponse(
            "VALIDATION_ERROR",
            message,
            null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<ErrorResponse> handleRestClientException(RestClientException e) {
        logger.error("Erreur lors de l'appel REST: {}", e.getMessage(), e);
        ErrorResponse error = new ErrorResponse(
            "SERVICE_UNAVAILABLE",
            "Le service de validation n'est pas disponible",
            null
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        logger.error("Erreur inattendue: {}", e.getMessage(), e);
        ErrorResponse error = new ErrorResponse(
            "INTERNAL_ERROR",
            "Une erreur interne s'est produite",
            null
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
