package com.prescription.demo.model;

import java.util.List;

public class PrescriptionRequest {

    private String prescriptionId;
    private List<DrugItem> drugs;

    public static class DrugItem {
        private String name;
        private int quantity;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
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