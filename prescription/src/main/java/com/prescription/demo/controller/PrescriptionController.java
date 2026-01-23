package main.java.com.prescription.demo.controller;

import com.prescription.demo.model.PrescriptionRequest;
import com.prescription.demo.model.PrescriptionResult;
import com.prescription.demo.service.PrescriptionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @PostMapping
    public PrescriptionResult create(@RequestBody PrescriptionRequest request) {
        return prescriptionService.process(request);
    }
}