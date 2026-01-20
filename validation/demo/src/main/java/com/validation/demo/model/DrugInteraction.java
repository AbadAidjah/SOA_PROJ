package com.validation.demo.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "drug_interaction")
@IdClass(DrugInteraction.DrugInteractionId.class)
public class DrugInteraction {
    @Id
    @Column(name = "drug_a")
    private String drugA;

    @Id
    @Column(name = "drug_b")
    private String drugB;

    @Column(nullable = false)
    private String level;

    public DrugInteraction() {}
    public DrugInteraction(String drugA, String drugB, String level) {
        this.drugA = drugA;
        this.drugB = drugB;
        this.level = level;
    }
    public String getDrugA() { 
        return drugA;

     }
    public void setDrugA(String drugA) {
         this.drugA = drugA;
         }
    public String getDrugB() {
         return drugB;
         }
    public void setDrugB(String drugB) { 
        this.drugB = drugB;
     }
    public String getLevel() {
         return level; 
        }
    public void setLevel(String level) { 
        this.level = level;
     }

    public static class DrugInteractionId implements Serializable {
        private String drugA;
        private String drugB;
        public DrugInteractionId() {}
        public DrugInteractionId(String drugA, String drugB) {
            this.drugA = drugA;
            this.drugB = drugB;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DrugInteractionId that = (DrugInteractionId) o;
            return Objects.equals(drugA, that.drugA) && Objects.equals(drugB, that.drugB);
        }
        @Override
        public int hashCode() {
            return Objects.hash(drugA, drugB);
        }
    }
}
