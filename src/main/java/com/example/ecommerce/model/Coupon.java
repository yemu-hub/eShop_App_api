package com.example.ecommerce.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "coupons")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(name = "code", unique = true)
    private String code;
    
    @Column(name = "description")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false)
    private DiscountType discountType;
    
    @NotNull
    @Column(name = "discount_value", nullable = false)
    private BigDecimal discountValue;
    
    @Column(name = "minimum_purchase")
    private BigDecimal minimumPurchase;
    
    @Column(name = "maximum_discount")
    private BigDecimal maximumDiscount;
    
    @Column(name = "usage_limit")
    private Integer usageLimit;
    
    @Column(name = "usage_count")
    private Integer usageCount = 0;
    
    @Column(name = "start_date")
    private LocalDateTime startDate;
    
    @Column(name = "end_date")
    private LocalDateTime endDate;
    
    @Column(name = "is_active")
    private boolean active = true;
    
    @ElementCollection
    @CollectionTable(name = "coupon_applicable_categories", 
        joinColumns = @JoinColumn(name = "coupon_id"))
    @Column(name = "category_id")
    private Set<String> applicableCategories = new HashSet<>();
    
    @ElementCollection
    @CollectionTable(name = "coupon_applicable_products", 
        joinColumns = @JoinColumn(name = "coupon_id"))
    @Column(name = "product_id")
    private Set<String> applicableProducts = new HashSet<>();
    
    @Column(name = "user_specific")
    private boolean userSpecific = false;
    
    @ElementCollection
    @CollectionTable(name = "coupon_allowed_users", 
        joinColumns = @JoinColumn(name = "coupon_id"))
    @Column(name = "user_id")
    private Set<String> allowedUsers = new HashSet<>();
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public boolean isValid() {
        LocalDateTime now = LocalDateTime.now();
        return active &&
               (usageLimit == null || usageCount < usageLimit) &&
               (startDate == null || now.isAfter(startDate)) &&
               (endDate == null || now.isBefore(endDate));
    }
    
    public boolean isApplicableToProduct(Long productId) {
        return applicableProducts.isEmpty() || applicableProducts.contains(productId);
    }
    
    public boolean isApplicableToCategory(String categoryId) {
        return applicableCategories.isEmpty() || applicableCategories.contains(categoryId);
    }
    
    public boolean isApplicableToUser(Long userId) {
        return !userSpecific || allowedUsers.contains(userId);
    }
    
    public BigDecimal calculateDiscount(BigDecimal originalAmount) {
        if (!isValid() || originalAmount.compareTo(minimumPurchase) < 0) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal discount = discountType == DiscountType.PERCENTAGE ?
            originalAmount.multiply(discountValue.divide(new BigDecimal("100"))) :
            discountValue;
            
        if (maximumDiscount != null && discount.compareTo(maximumDiscount) > 0) {
            return maximumDiscount;
        }
        
        return discount;
    }
} 