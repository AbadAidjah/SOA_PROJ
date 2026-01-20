package com.validation.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.validation.demo.model.Drug;

import java.util.List;
import java.util.Optional;

public interface DrugRepository extends JpaRepository<Drug, String> {
    Optional<Drug> findByName(String name);
    List<Drug> findByNameIn(List<String> names);
}
