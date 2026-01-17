package com.pharmacie.demo.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pharmacie.demo.model.StockItem;

public interface StockItemRepository extends JpaRepository<StockItem, UUID>{
    
}
