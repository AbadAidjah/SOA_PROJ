package com.pharmacie.demo.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class StockItem {
    @Id
    private UUID id;
    private String drugCode;
    private int quantityAvailable;

    public StockItem() {
        this.id = UUID.randomUUID();
    }


public UUID getId() {
    return id;
}

public void setId(UUID id) {
    this.id = id;
}

public String getDrugCode() {
    return drugCode;
}

public void setDrugCode(String drugCode) {
    this.drugCode = drugCode;
}

public int getQuantityAvailable() {
    return quantityAvailable;
}

public void setQuantityAvailable(int quantityAvailable) {
    this.quantityAvailable = quantityAvailable;
}
}
