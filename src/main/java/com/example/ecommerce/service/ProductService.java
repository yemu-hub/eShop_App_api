package com.example.ecommerce.service;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.dto.CategoryDTO;
import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    List<Product> searchProducts(String query);
    Product getProductById(Long id);
    Product createProduct(Product product);
    Product updateProduct(Long id, Product product);
    void deleteProduct(Long id);
    void updateProductStatus(Long id, boolean active);
    List<CategoryDTO> getAllCategories();
    List<Product> getProductsByVendor(Long vendorId);
    List<Product> getProductsByCategory(Long categoryId);
    /**
     * Get products by their availability status.
     * @param status "AVAILABLE" for available products, any other value for unavailable products
     * @return List of products matching the availability status
     */
    List<Product> getProductsByStatus(String status);
    boolean existsById(Long id);
} 