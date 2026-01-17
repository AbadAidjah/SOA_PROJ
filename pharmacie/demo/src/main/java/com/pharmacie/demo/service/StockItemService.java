package com.pharmacie.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.pharmacie.demo.model.StockItem;
import com.pharmacie.demo.repository.StockItemRepository;
@Service
public class StockItemService {
    
    private final StockItemRepository stockItemRepository;

    public StockItemService(StockItemRepository stockItemRepository) {
        this.stockItemRepository = stockItemRepository;
    }

    public List<StockItem> getAllStockItems() {
        return stockItemRepository.findAll();
    }

    public Optional<StockItem> getStockItemById(UUID id) {
        return stockItemRepository.findById(id);
    }

    public StockItem saveStockItem(StockItem stockItem) {
        return stockItemRepository.save(stockItem);
    }

    public void deleteStockItem(UUID id) {
        stockItemRepository.deleteById(id);
    }
}
