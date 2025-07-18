package com.example.ecommerce.model;

public enum OrderStatus {
    PENDING,           // Initial state when order is created
    STOCK_PENDING,     // Waiting for stock to be available
    STOCK_ARRANGING,   // Admin is arranging stock
    PAYMENT_PENDING,   // Waiting for advance payment
    READY_TO_SHIP,     // Payment received, preparing for dispatch
    SHIPPED,           // Order has been dispatched
    DELIVERED,         // Order has been delivered
    RETURN_PENDING,    // Order is marked for return
    RETURNED,          // Order has been returned
    REJECTED,          // Order is marked as invalid
    APPROVED,          // Admin has approved the order
    CANCELLED,         // Order has been cancelled
    COMPLETED         // Order has been completed
} 