package com.example.ecommerce.controller;

import com.example.ecommerce.dto.CatalogDTO;
import com.example.ecommerce.model.Catalog;
import com.example.ecommerce.service.CatalogService;
import com.example.ecommerce.mapper.CatalogMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/api/catalogs")
@RequiredArgsConstructor
public class CatalogController {
    private final CatalogService catalogService;
    private final CatalogMapper catalogMapper;

    @PostMapping
    public ResponseEntity<CatalogDTO> createCatalog(@Valid @RequestBody CatalogDTO catalogDTO) {
        Catalog catalog = catalogMapper.toEntity(catalogDTO);
        Catalog createdCatalog = catalogService.createCatalog(catalog);
        return ResponseEntity.ok(catalogMapper.toDTO(createdCatalog));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatalogDTO> getCatalogById(@PathVariable Integer id) {
        Catalog catalog = catalogService.getCatalogById(id);
        return ResponseEntity.ok(catalogMapper.toDTO(catalog));
    }

    @GetMapping
    public ResponseEntity<List<CatalogDTO>> getAllCatalogs() {
        List<Catalog> catalogs = catalogService.getAllCatalogs();
        return ResponseEntity.ok(catalogMapper.toDTOList(catalogs));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<CatalogDTO>> getCatalogsByCategory(@PathVariable String category) {
        List<Catalog> catalogs = catalogService.getCatalogsByCategory(category);
        return ResponseEntity.ok(catalogMapper.toDTOList(catalogs));
    }

    @GetMapping("/active")
    public ResponseEntity<List<CatalogDTO>> getActiveCatalogs() {
        List<Catalog> catalogs = catalogService.getActiveCatalogs();
        return ResponseEntity.ok(catalogMapper.toDTOList(catalogs));
    }

    @GetMapping("/search")
    public ResponseEntity<List<CatalogDTO>> searchCatalogs(@RequestParam String name) {
        List<Catalog> catalogs = catalogService.searchCatalogsByName(name);
        return ResponseEntity.ok(catalogMapper.toDTOList(catalogs));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CatalogDTO> updateCatalog(
        @PathVariable Integer id,
        @Valid @RequestBody CatalogDTO catalogDTO
    ) {
        Catalog catalog = catalogMapper.toEntity(catalogDTO);
        Catalog updatedCatalog = catalogService.updateCatalog(id, catalog);
        return ResponseEntity.ok(catalogMapper.toDTO(updatedCatalog));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCatalog(@PathVariable Integer id) {
        catalogService.deleteCatalog(id);
        return ResponseEntity.noContent().build();
    }
} 