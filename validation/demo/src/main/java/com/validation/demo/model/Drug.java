package com.validation.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "drug")
public class Drug {
    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String name;

    public Drug() {}
    public Drug(String id, String name) {
        this.id = id;
        this.name = name;
    }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
