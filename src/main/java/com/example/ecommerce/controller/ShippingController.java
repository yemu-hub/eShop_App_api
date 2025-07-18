package com.example.ecommerce.controller;

import com.example.ecommerce.dto.ShippingDTO;
import com.example.ecommerce.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shippings")
public class ShippingController {

    @Autowired
    private ShippingService shippingService;

    @PostMapping
    public ResponseEntity<ShippingDTO> createShipping(@RequestBody ShippingDTO shippingDTO) {
        ShippingDTO createdShipping = shippingService.createShipping(shippingDTO);
        return ResponseEntity.ok(createdShipping);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShippingDTO> getShippingById(@PathVariable Long id) {
        ShippingDTO shipping = shippingService.getShippingById(id);
        return ResponseEntity.ok(shipping);
    }

    @GetMapping
    public ResponseEntity<List<ShippingDTO>> getAllShippings() {
        List<ShippingDTO> shippings = shippingService.getAllShippings();
        return ResponseEntity.ok(shippings);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ShippingDTO>> getShippingsByStatus(@PathVariable String status) {
        List<ShippingDTO> shippings = shippingService.getShippingsByStatus(status);
        return ResponseEntity.ok(shippings);
    }

    @GetMapping("/method/{method}")
    public ResponseEntity<List<ShippingDTO>> getShippingsByMethod(@PathVariable String method) {
        List<ShippingDTO> shippings = shippingService.getShippingsByMethod(method);
        return ResponseEntity.ok(shippings);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShippingDTO> updateShipping(@PathVariable Long id, @RequestBody ShippingDTO shippingDTO) {
        ShippingDTO updatedShipping = shippingService.updateShipping(id, shippingDTO);
        return ResponseEntity.ok(updatedShipping);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ShippingDTO> updateShippingStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        ShippingDTO updatedShipping = shippingService.updateShippingStatus(id, status);
        return ResponseEntity.ok(updatedShipping);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShipping(@PathVariable Long id) {
        shippingService.deleteShipping(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<ShippingDTO>> getShippingsByOrder(@PathVariable Long orderId) {
        List<ShippingDTO> shippings = shippingService.getShippingsByOrder(orderId);
        return ResponseEntity.ok(shippings);
    }

    @GetMapping("/tracking/{trackingNumber}")
    public ResponseEntity<List<ShippingDTO>> getShippingsByTrackingNumber(@PathVariable String trackingNumber) {
        List<ShippingDTO> shippings = shippingService.getShippingsByTrackingNumber(trackingNumber);
        return ResponseEntity.ok(shippings);
    }

    @GetMapping("/country/{country}")
    public ResponseEntity<List<ShippingDTO>> getShippingsByCountry(@PathVariable String country) {
        List<ShippingDTO> shippings = shippingService.getShippingsByCountry(country);
        return ResponseEntity.ok(shippings);
    }

    @GetMapping("/state/{state}")
    public ResponseEntity<List<ShippingDTO>> getShippingsByState(@PathVariable String state) {
        List<ShippingDTO> shippings = shippingService.getShippingsByState(state);
        return ResponseEntity.ok(shippings);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ShippingDTO>> getShippingsByStatusAndMethod(
            @RequestParam String status,
            @RequestParam String method) {
        List<ShippingDTO> shippings = shippingService.getShippingsByStatusAndMethod(status, method);
        return ResponseEntity.ok(shippings);
    }
} 