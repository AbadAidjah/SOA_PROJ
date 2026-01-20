package com.validation.demo.controller;

import com.validation.demo.model.Drug;
import com.validation.demo.service.ValidationService;
import com.validation.demo.service.ValidationService.ValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DrugController {

    @Autowired
    private ValidationService validationService;

    
    @GetMapping("/drugs")
    public List<Drug> getAllDrugs() {
        return validationService.getAllDrugs();
    }

    
    @PostMapping("/validate")
    public ResponseEntity<ValidationResult> validateDrugs(@RequestBody Map<String, List<String>> request) {
        List<String> drugNames = request.get("drugNames");
        if (drugNames == null || drugNames.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        ValidationResult result = validationService.validateDrugs(drugNames);
        return ResponseEntity.ok(result);
    }
}
