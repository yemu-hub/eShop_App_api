package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.CatalogDTO;
import com.example.ecommerce.model.Catalog;
import com.example.ecommerce.repository.CatalogRepository;
import com.example.ecommerce.service.CatalogService;
import com.example.ecommerce.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService {
    private final CatalogRepository catalogRepository;

    @Override
    public List<Catalog> getAllCatalogs() {
        return catalogRepository.findAll();
    }

    @Override
    public Catalog getCatalogById(Integer id) {
        return catalogRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Catalog not found with id: " + id));
    }

    @Override
    @Transactional
    public Catalog createCatalog(Catalog catalog) {
        return catalogRepository.save(catalog);
    }

    @Override
    @Transactional
    public Catalog updateCatalog(Integer id, Catalog catalog) {
        Catalog existingCatalog = getCatalogById(id);
        
        existingCatalog.setName(catalog.getName());
        existingCatalog.setDescription(catalog.getDescription());
        existingCatalog.setCategory(catalog.getCategory());
        existingCatalog.setSeason(catalog.getSeason());
        existingCatalog.setStartDate(catalog.getStartDate());
        existingCatalog.setEndDate(catalog.getEndDate());
        existingCatalog.setActive(catalog.isActive());
        existingCatalog.setImportance(catalog.getImportance());
        
        return catalogRepository.save(existingCatalog);
    }

    @Override
    @Transactional
    public void deleteCatalog(Integer id) {
        if (!catalogRepository.existsById(id)) {
            throw new ResourceNotFoundException("Catalog not found with id: " + id);
        }
        catalogRepository.deleteById(id);
    }

    @Override
    public List<Catalog> getCatalogsByCategory(String category) {
        return catalogRepository.findByCategory(category);
    }

    @Override
    public List<Catalog> getActiveCatalogs() {
        return catalogRepository.findByActive(true);
    }

    @Override
    public List<Catalog> searchCatalogsByName(String name) {
        return catalogRepository.findByNameContainingIgnoreCase(name);
    }
} 