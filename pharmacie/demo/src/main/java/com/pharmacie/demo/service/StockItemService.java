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
    public Integer getQuantityAvailableByDrugCode(String DrugCode){
        Optional<StockItem> item = stockItemRepository.findByDrugCode(DrugCode);
        return item.map(StockItem::getQuantityAvailable).orElse(null);
    }
    public boolean setQuantityAvailableByDrugCode(String DrugCode, int newQty){
        Optional<StockItem> itemOpt = stockItemRepository.findByDrugCode(DrugCode);
        if(itemOpt.isPresent()){
            StockItem item = itemOpt.get();
            int currentQty = item.getQuantityAvailable();
            if(currentQty >= newQty){
                item.setQuantityAvailable(currentQty - newQty);
                stockItemRepository.save(item);
                return true;
            }
            
        }
        return false;
    }

    public StockItem saveStockItem(StockItem stockItem) {
        return stockItemRepository.save(stockItem);
    }

    public void deleteStockItem(UUID id) {
        stockItemRepository.deleteById(id);
    }
}
