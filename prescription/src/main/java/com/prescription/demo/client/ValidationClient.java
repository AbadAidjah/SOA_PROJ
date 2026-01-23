package com.prescription.demo.client;

import com.prescription.demo.model.PrescriptionRequest;
import com.prescription.demo.model.ValidationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Client REST pour communiquer avec le service de validation
 */
@Component
public class ValidationClient {

    private static final Logger logger = LoggerFactory.getLogger(ValidationClient.class);

    private final RestTemplate restTemplate;
    private final String VALIDATION_URL = "http://validation-service:8081/api/validate";

    public ValidationClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Valide une prescription auprès du service de validation
     * 
     * @param request La demande de prescription
     * @return La réponse de validation
     * @throws RestClientException Si l'appel REST échoue
     */
    public ValidationResponse validate(PrescriptionRequest request) {
        try {
            logger.debug("Envoi de la requête de validation à: {}", VALIDATION_URL);
            
            // Le service de validation attend {"drugs": [...]}
            Map<String, List<Map<String, Object>>> requestBody = new HashMap<>();
            List<Map<String, Object>> drugs = request.getDrugs().stream()
                .map(drug -> {
                    Map<String, Object> drugMap = new HashMap<>();
                    drugMap.put("name", drug.getName());
                    drugMap.put("quantity", drug.getQuantity());
                    return drugMap;
                })
                .collect(Collectors.toList());
            requestBody.put("drugs", drugs);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, List<Map<String, Object>>>> entity = 
                new HttpEntity<>(requestBody, headers);

            ResponseEntity<ValidationResponse> response = restTemplate.postForEntity(
                VALIDATION_URL,
                entity,
                ValidationResponse.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                logger.debug("Validation réussie. OK: {}", response.getBody().isOk());
                return response.getBody();
            } else {
                logger.error("Réponse invalide du service de validation: {}", response.getStatusCode());
                throw new RestClientException("Réponse invalide du service de validation");
            }

        } catch (RestClientException e) {
            logger.error("Erreur lors de l'appel au service de validation: {}", e.getMessage(), e);
            throw e;
        }
    }
}