package com.example.ecommerce.repository;

import com.example.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByStockQuantityLessThan(int threshold);
    List<Product> findByStockQuantityGreaterThan(int threshold);
    List<Product> findByNameContainingIgnoreCase(String query);
    List<Product> findByCategory(String category);
    List<Product> findByVendorId(Long vendorId);
    
    @Query("SELECT p FROM Product p JOIN p.categories c WHERE c.id = :categoryId")
    List<Product> findByCategoryId(@Param("categoryId") Long categoryId);
    
    List<Product> findByActive(boolean active);
} 