package com.validation.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.validation.demo.model.Drug;
import com.validation.demo.service.ValidationService;
import com.validation.demo.service.ValidationService.ValidationResult;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DrugController {

    @Autowired
    private ValidationService validationService;

    public static class DrugRequest {
    public String name;
    public int quantity;
    }
    
    @GetMapping("/drugs")
    public List<Drug> getAllDrugs() {
        return validationService.getAllDrugs();
    }
    @GetMapping("/drugsLimit")
public List<Drug> getDrugs(
        @RequestParam(value = "search", required = false) String search,
        @RequestParam(value = "limit", required = false, defaultValue = "50") int limit) {
    List<Drug> allDrugs = validationService.getAllDrugs();
    // Filter by search if provided
    if (search != null && !search.isEmpty()) {
        allDrugs = allDrugs.stream()
                .filter(drug -> drug.getName().toLowerCase().contains(search.toLowerCase()))
                .toList();
    }
    // Limit the result
    return allDrugs.stream().limit(limit).toList();
}

    
    @PostMapping("/validate")
public ResponseEntity<ValidationResult> validateDrugs(@RequestBody Map<String, List<DrugRequest>> request) {
    List<DrugRequest> drugs = request.get("drugs");
    if (drugs == null || drugs.isEmpty()) {
        return ResponseEntity.badRequest().build();
    }
    ValidationResult result = validationService.validateDrugs(drugs);
    return ResponseEntity.ok(result);
}
}
