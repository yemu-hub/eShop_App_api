package com.example.ecommerce.service.impl;

import com.example.ecommerce.model.Inventory;
import com.example.ecommerce.repository.InventoryRepository;
import com.example.ecommerce.service.InventoryService;
import com.example.ecommerce.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Override
    @Transactional
    public Inventory addInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    @Override
    @Transactional
    public Inventory updateInventory(Long id, Inventory inventory) {
        Inventory existingInventory = getInventoryById(id);
        existingInventory.setProduct(inventory.getProduct());
        existingInventory.setWarehouse(inventory.getWarehouse());
        existingInventory.setPrice(inventory.getPrice());
        existingInventory.setTitle(inventory.getTitle());
        existingInventory.setTimestamp(inventory.getTimestamp());
        return inventoryRepository.save(existingInventory);
    }

    @Override
    @Transactional
    public void deleteInventory(Long id) {
        if (!inventoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Inventory not found with id: " + id);
        }
        inventoryRepository.deleteById(id);
    }

    @Override
    public Inventory getInventoryById(Long id) {
        return inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found with id: " + id));
    }

    @Override
    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    @Override
    public List<Inventory> getInventoryByProductId(Long productId) {
        return inventoryRepository.findByProductId(productId);
    }

    @Override
    public List<Inventory> getInventoryByWarehouseId(Long warehouseId) {
        return inventoryRepository.findByWarehouseId(warehouseId);
    }

    @Override
    @Transactional
    public void updateStock(Long productId, Long warehouseId, int quantity) {
        Inventory inventory = inventoryRepository.findByProductIdAndWarehouseId(productId, warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product id: " + productId + " and warehouse id: " + warehouseId));
        // Here you might want to add business logic for stock updates
        // For example, checking if the quantity is valid, etc.
        inventory.setTitle(quantity); // Assuming title is used for quantity
        inventoryRepository.save(inventory);
    }
} 