package com.example.ecommerce.controller;

import com.example.ecommerce.dto.VendorDTO;
import com.example.ecommerce.service.VendorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/api/vendors")
@RequiredArgsConstructor
public class VendorController {
    private final VendorService vendorService;

    @GetMapping
    public ResponseEntity<List<VendorDTO>> getAllVendors() {
        return ResponseEntity.ok(vendorService.getAllVendors());
    }

    @PostMapping
    public ResponseEntity<VendorDTO> createVendor(@Valid @RequestBody VendorDTO vendorDTO) {
        return ResponseEntity.ok(vendorService.createVendor(vendorDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendorDTO> getVendorById(@PathVariable Long id) {
        return ResponseEntity.ok(vendorService.getVendorById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VendorDTO> updateVendor(
        @PathVariable Long id,
        @Valid @RequestBody VendorDTO vendorDTO
    ) {
        return ResponseEntity.ok(vendorService.updateVendor(id, vendorDTO));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<VendorDTO> updateVendorStatus(
        @PathVariable Long id,
        @RequestParam String status
    ) {
        return ResponseEntity.ok(vendorService.updateVendorStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVendor(@PathVariable Long id) {
        vendorService.deleteVendor(id);
        return ResponseEntity.noContent().build();
    }
}