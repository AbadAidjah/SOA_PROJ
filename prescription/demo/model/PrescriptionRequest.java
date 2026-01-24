package com.prescription.demo.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

/**
 * Modèle de requête pour une prescription
 */
public class PrescriptionRequest {

    @NotBlank(message = "L'ID de prescription est requis")
    private String prescriptionId;

    @NotEmpty(message = "Au moins un médicament est requis")
    @Valid
    private List<DrugItem> drugs;

    public static class DrugItem {
        @NotBlank(message = "Le nom du médicament est requis")
        private String name;

        @NotNull(message = "La quantité est requise")
        @Positive(message = "La quantité doit être positive")
        private Integer quantity;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public List<DrugItem> getDrugs() {
        return drugs;
    }

    public void setDrugs(List<DrugItem> drugs) {
        this.drugs = drugs;
    }
}