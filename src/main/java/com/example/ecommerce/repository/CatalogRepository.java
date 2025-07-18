package com.example.ecommerce.repository;

import com.example.ecommerce.model.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CatalogRepository extends JpaRepository<Catalog, Integer> {
    List<Catalog> findByCategory(String category);
    List<Catalog> findByActive(Boolean active);
    List<Catalog> findByNameContainingIgnoreCase(String name);
} 