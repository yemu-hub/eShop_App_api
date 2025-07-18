package com.example.ecommerce.service;

import com.example.ecommerce.model.Inventory;
import java.util.List;

public interface InventoryService {
    Inventory addInventory(Inventory inventory);
    Inventory updateInventory(Long id, Inventory inventory);
    void deleteInventory(Long id);
    Inventory getInventoryById(Long id);
    List<Inventory> getAllInventory();
    List<Inventory> getInventoryByProductId(Long productId);
    List<Inventory> getInventoryByWarehouseId(Long warehouseId);
    void updateStock(Long productId, Long warehouseId, int quantity);
} 