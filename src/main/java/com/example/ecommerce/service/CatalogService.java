package com.example.ecommerce.service;

import com.example.ecommerce.model.Catalog;
import java.util.List;

public interface CatalogService {
    List<Catalog> getAllCatalogs();
    Catalog getCatalogById(Integer id);
    Catalog createCatalog(Catalog catalog);
    Catalog updateCatalog(Integer id, Catalog catalog);
    void deleteCatalog(Integer id);
    List<Catalog> getCatalogsByCategory(String category);
    List<Catalog> getActiveCatalogs();
    List<Catalog> searchCatalogsByName(String name);
} 