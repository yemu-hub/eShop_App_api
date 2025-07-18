package com.example.ecommerce.service.impl;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.dto.CategoryDTO;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.service.ProductService;
import com.example.ecommerce.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    @Override
    public List<Product> searchProducts(String query) {
        return productRepository.findByNameContainingIgnoreCase(query);
    }
    
    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }
    
    @Override
    @Transactional
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }
    
    @Override
    @Transactional
    public Product updateProduct(Long id, Product product) {
        Product existingProduct = getProductById(id);
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setCost(product.getCost());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setStockQuantity(product.getStockQuantity());
        existingProduct.setActive(product.isActive());
        existingProduct.setVendor(product.getVendor());
        return productRepository.save(existingProduct);
    }
    
    @Override
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
    
    @Override
    public void updateProductStatus(Long id, boolean active) {
        Product product = getProductById(id);
        product.setActive(active);
        productRepository.save(product);
    }
    
    @Override
    public List<CategoryDTO> getAllCategories() {
        return productRepository.findAll().stream()
            .map(Product::getCategory)
            .distinct()
            .map(category -> {
                CategoryDTO dto = new CategoryDTO();
                dto.setName(category);
                dto.setActive(true);
                return dto;
            })
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Product> getProductsByVendor(Long vendorId) {
        return productRepository.findByVendorId(vendorId);
    }
    
    @Override
    public List<Product> getProductsByCategory(Long categoryId) {
        // Since we're using a string category, we'll need to modify this
        throw new UnsupportedOperationException("Method not supported with string categories");
    }

    @Override
    public List<Product> getProductsByStatus(String status) {
        boolean active = "AVAILABLE".equalsIgnoreCase(status);
        return productRepository.findByActive(active);
    }

    @Override
    public boolean existsById(Long id) {
        return productRepository.existsById(id);
    }
} 