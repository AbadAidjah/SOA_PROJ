package com.pharmacie.demo.repository;


import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pharmacie.demo.model.StockItem;
import java.util.List;


public interface StockItemRepository extends JpaRepository<StockItem, UUID>{
 Optional<StockItem> findByDrugCode(String drugCode);   
}
