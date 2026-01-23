package main.java.com.prescription.demo.client;

import com.prescription.demo.model.PrescriptionRequest;
import com.prescription.demo.model.ValidationResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ValidationClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String VALIDATION_URL =
            "http://validation-service:8081/api/validate";

    public ValidationResponse validate(PrescriptionRequest request) {
        return restTemplate.postForObject(
                VALIDATION_URL,
                request,
                ValidationResponse.class
        );
    }
}