package com.prescription.demo.exception;

import com.prescription.demo.model.ValidationResponse;
import java.util.List;

/**
 * Exception levée lorsqu'une validation échoue (interactions médicamenteuses)
 */
public class ValidationException extends RuntimeException {
    
    private final List<ValidationResponse.Issue> issues;

    public ValidationException(String message) {
        super(message);
        this.issues = null;
    }

    public ValidationException(String message, List<ValidationResponse.Issue> issues) {
        super(message);
        this.issues = issues;
    }

    public List<ValidationResponse.Issue> getIssues() {
        return issues;
    }
}
