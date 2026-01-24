package com.prescription.demo.model;

import com.prescription.demo.model.ValidationResponse;
import java.util.List;

/**
 * Modèle de réponse d'erreur standardisé
 */
public class ErrorResponse {
    
    private String code;
    private String message;
    private List<ValidationResponse.Issue> issues;

    public ErrorResponse() {
    }

    public ErrorResponse(String code, String message, List<ValidationResponse.Issue> issues) {
        this.code = code;
        this.message = message;
        this.issues = issues;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ValidationResponse.Issue> getIssues() {
        return issues;
    }

    public void setIssues(List<ValidationResponse.Issue> issues) {
        this.issues = issues;
    }
}
