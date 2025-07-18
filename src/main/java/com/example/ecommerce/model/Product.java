package com.example.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(length = 1000)
    private String description;
    
    @Column(nullable = false)
    private BigDecimal price;
    
    @Column(nullable = false)
    private BigDecimal cost;
    
    @Column(nullable = false)
    private String category;
    
    @Column(nullable = false)
    private Integer stockQuantity;
    
    private boolean active = true;
    
    @ManyToOne
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;
    
    @ManyToOne
    @JoinColumn(name = "catalog_id")
    private Catalog catalog;
    
    @ManyToMany
    @JoinTable(
        name = "product_categories",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories = new ArrayList<>();
    
    // Compatibility methods for existing code
    public Integer getStock() {
        return this.stockQuantity;
    }
    
    public void setStock(Integer stock) {
        this.stockQuantity = stock;
    }
    
    public boolean isAvailable() {
        return this.active;
    }
    
    public void setAvailable(boolean available) {
        this.active = available;
    }
} 