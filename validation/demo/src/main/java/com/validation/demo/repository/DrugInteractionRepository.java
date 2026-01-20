package com.validation.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.validation.demo.model.DrugInteraction;

import java.util.Optional;

public interface DrugInteractionRepository extends JpaRepository<DrugInteraction, DrugInteraction.DrugInteractionId> {
    Optional<DrugInteraction> findByDrugAAndDrugB(String drugA, String drugB);
}
