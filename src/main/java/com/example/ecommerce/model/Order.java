package com.example.ecommerce.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    
    @Column(nullable = false)
    private LocalDate orderDate;
    
    @Column(nullable = false)
    private BigDecimal totalAmount;
    
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();
    
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type")
    private PaymentType paymentType;
    
    @Column(name = "advance_payment", precision = 10, scale = 2)
    private BigDecimal advancePayment;
    
    @Column(name = "stock_verified")
    private boolean stockVerified;
    
    @Column(name = "stock_available")
    private boolean stockAvailable;
    
    @Column(name = "stock_arrangement_notes", columnDefinition = "TEXT")
    private String stockArrangementNotes;
    
    @Column(name = "shipped")
    private boolean shipped;
    
    @Column(name = "delivered")
    private boolean delivered;
    
    @Column(name = "returned")
    private boolean returned;
    
    @Column(name = "return_reason", columnDefinition = "TEXT")
    private String returnReason;
    
    @Column(name = "admin_notes", columnDefinition = "TEXT")
    private String adminNotes;
    
    @Column(name = "valid")
    private boolean valid;
    
    @Column(name = "admin_approved")
    private boolean adminApproved;
    
    @Column(name = "admin_signature")
    private String adminSignature;
    
    @Column(name = "estimated_delivery_date")
    private LocalDateTime estimatedDeliveryDate;
    
    @Column(name = "actual_delivery_date")
    private LocalDateTime actualDeliveryDate;
    
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
    
    public void calculateTotalAmount() {
        this.totalAmount = orderItems.stream()
            .map(OrderItem::getSubTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Compatibility methods
    public List<OrderItem> getItems() {
        return this.orderItems;
    }
    
    public void setItems(List<OrderItem> items) {
        this.orderItems = items;
    }
} 