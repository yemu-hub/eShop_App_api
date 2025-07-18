package com.example.ecommerce.service;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.OrderItem;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    // Customer operations
    Order createOrder(Order order);
    Order getOrderById(Long id);
    List<Order> getAllOrders();
    List<Order> getOrdersByUser(Long userId);
    
    // Admin operations
    List<Order> getPendingOrders();
    
    // Stock verification and arrangement
    Order verifyStock(String orderId);
    Order arrangeProduct(String orderId);
    Order markStockAvailable(String orderId);
    
    // Shipping and delivery
    Order shipOrder(String orderId);
    Order verifyCustomerAndDeliver(String orderId);
    
    // Payment processing
    Order processCODPayment(String orderId, BigDecimal amount);
    Order processAdvancePayment(String orderId, BigDecimal amount);
    boolean isAdvancePaymentReceived(String orderId);
    BigDecimal calculateRequiredAdvancePayment(String orderId);
    
    // Return handling
    Order initiateReturn(String orderId, String reason);
    Order processReturn(String orderId);
    
    // Admin operations
    Order reviewOrder(String orderId, boolean isValid, String adminNotes);
    Order approveOrder(String orderId, String adminSignature);
    Order cancelOrder(String orderId, String reason);
    
    // Stock management
    boolean isStockAvailable(String orderId);
    void requestStockFromVendor(String orderId);
    void updateStockLevel(Long productId, int quantity);
    
    // Delivery management
    void updateEstimatedDeliveryDate(String orderId, LocalDateTime estimatedDate);
    void updateActualDeliveryDate(String orderId, LocalDateTime actualDate);

    Order updateOrder(Long id, Order order);
    void deleteOrder(Long id);
} 