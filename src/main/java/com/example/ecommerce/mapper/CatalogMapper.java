package com.example.ecommerce.mapper;

import com.example.ecommerce.dto.CatalogDTO;
import com.example.ecommerce.model.Catalog;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CatalogMapper {
    
    public CatalogDTO toDTO(Catalog catalog) {
        if (catalog == null) {
            return null;
        }

        CatalogDTO dto = new CatalogDTO();
        dto.setId(catalog.getId());
        dto.setName(catalog.getName());
        dto.setDescription(catalog.getDescription());
        dto.setCategory(catalog.getCategory());
        dto.setSeason(catalog.getSeason());
        dto.setStartDate(catalog.getStartDate());
        dto.setEndDate(catalog.getEndDate());
        dto.setActive(catalog.isActive());
        dto.setImportance(catalog.getImportance());
        
        return dto;
    }

    public Catalog toEntity(CatalogDTO dto) {
        if (dto == null) {
            return null;
        }

        Catalog catalog = new Catalog();
        // Don't set ID for new catalogs
        if (dto.getId() != null) {
            catalog.setId(dto.getId());
        }
        catalog.setName(dto.getName());
        catalog.setDescription(dto.getDescription());
        catalog.setCategory(dto.getCategory());
        catalog.setSeason(dto.getSeason());
        catalog.setStartDate(dto.getStartDate());
        catalog.setEndDate(dto.getEndDate());
        catalog.setActive(dto.isActive());
        catalog.setImportance(dto.getImportance());
        
        return catalog;
    }

    public List<CatalogDTO> toDTOList(List<Catalog> catalogs) {
        return catalogs.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
} 