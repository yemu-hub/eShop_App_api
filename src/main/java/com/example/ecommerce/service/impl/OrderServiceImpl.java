package com.example.ecommerce.service.impl;

import com.example.ecommerce.model.*;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.CustomerRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.service.OrderService;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.exception.InvalidOperationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public Order createOrder(Order order) {
        validateOrder(order);
        updateProductStock(order);
        return orderRepository.save(order);
    }

    private void validateOrder(Order order) {
        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            if (product == null) {
                throw new IllegalArgumentException("Product not found for order item");
            }
            if (item.getQuantity() > product.getStockQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for product: " + product.getName());
            }
        }
    }

    private void updateProductStock(Order order) {
        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productRepository.save(product);
        }
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        Order order = getOrderById(id);
        restoreProductStock(order);
        orderRepository.delete(order);
    }

    private void restoreProductStock(Order order) {
        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            productRepository.save(product);
        }
    }

    @Override
    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByCustomerId(userId);
    }

    @Override
    public List<Order> getPendingOrders() {
        return orderRepository.findByStatus(OrderStatus.PENDING);
    }

    @Override
    @Transactional
    public Order verifyStock(String orderId) {
        Order order = orderRepository.findById(Long.parseLong(orderId))
            .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        
        boolean allItemsAvailable = order.getOrderItems().stream()
            .allMatch(item -> {
                Product product = item.getProduct();
                return product.getStockQuantity() >= item.getQuantity();
            });
        
        order.setStockVerified(true);
        order.setStockAvailable(allItemsAvailable);
        
        if (!allItemsAvailable) {
            order.setStatus(OrderStatus.STOCK_PENDING);
        }
        
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order arrangeProduct(String orderId) {
        Order order = getOrderById(Long.parseLong(orderId));
        if (!order.isStockVerified()) {
            throw new InvalidOperationException("Stock verification not done");
        }
        
        order.setStatus(OrderStatus.STOCK_ARRANGING);
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order markStockAvailable(String orderId) {
        Order order = getOrderById(Long.parseLong(orderId));
        order.setStockAvailable(true);
        
        if (order.getPaymentType() == PaymentType.PREPAID && !isAdvancePaymentReceived(orderId)) {
            order.setStatus(OrderStatus.PAYMENT_PENDING);
        } else {
            order.setStatus(OrderStatus.READY_TO_SHIP);
        }
        
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order shipOrder(String orderId) {
        Order order = getOrderById(Long.parseLong(orderId));
        
        if (!order.isStockAvailable()) {
            throw new InvalidOperationException("Stock not available");
        }
        
        if (order.getPaymentType() == PaymentType.PREPAID && !isAdvancePaymentReceived(orderId)) {
            throw new InvalidOperationException("Advance payment not received");
        }
        
        order.setShipped(true);
        order.setStatus(OrderStatus.SHIPPED);
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order verifyCustomerAndDeliver(String orderId) {
        Order order = getOrderById(Long.parseLong(orderId));
        
        if (!order.isShipped()) {
            throw new InvalidOperationException("Order not shipped yet");
        }
        
        order.setDelivered(true);
        order.setStatus(OrderStatus.DELIVERED);
        order.setActualDeliveryDate(LocalDateTime.now());
        
        if (order.getPaymentType() == PaymentType.COD) {
            order.setPaymentStatus(PaymentStatus.PENDING);
        }
        
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order processCODPayment(String orderId, BigDecimal amount) {
        Order order = getOrderById(Long.parseLong(orderId));
        
        if (order.getPaymentType() != PaymentType.COD) {
            throw new InvalidOperationException("Not a COD order");
        }
        
        if (!order.isDelivered()) {
            throw new InvalidOperationException("Order not delivered yet");
        }
        
        if (amount.compareTo(order.getTotalAmount()) != 0) {
            throw new InvalidOperationException("Invalid payment amount");
        }
        
        order.setPaymentStatus(PaymentStatus.COMPLETED);
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order processAdvancePayment(String orderId, BigDecimal amount) {
        Order order = getOrderById(Long.parseLong(orderId));
        
        if (order.getPaymentType() != PaymentType.PREPAID) {
            throw new InvalidOperationException("Not a prepaid order");
        }
        
        BigDecimal requiredAdvance = calculateRequiredAdvancePayment(orderId);
        if (amount.compareTo(requiredAdvance) < 0) {
            throw new InvalidOperationException("Insufficient advance payment");
        }
        
        order.setAdvancePayment(amount);
        order.setPaymentStatus(PaymentStatus.COMPLETED);
        
        if (order.isStockAvailable()) {
            order.setStatus(OrderStatus.READY_TO_SHIP);
        }
        
        return orderRepository.save(order);
    }

    @Override
    public boolean isAdvancePaymentReceived(String orderId) {
        Order order = getOrderById(Long.parseLong(orderId));
        return order.getPaymentStatus() == PaymentStatus.COMPLETED;
    }

    @Override
    public BigDecimal calculateRequiredAdvancePayment(String orderId) {
        Order order = getOrderById(Long.parseLong(orderId));
        return order.getTotalAmount();
    }

    @Override
    @Transactional
    public Order initiateReturn(String orderId, String reason) {
        Order order = getOrderById(Long.parseLong(orderId));
        
        if (!order.isDelivered()) {
            throw new InvalidOperationException("Order not delivered yet");
        }
        
        order.setReturned(true);
        order.setReturnReason(reason);
        order.setStatus(OrderStatus.RETURN_PENDING);
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order processReturn(String orderId) {
        Order order = getOrderById(Long.parseLong(orderId));
        
        if (!order.isReturned()) {
            throw new InvalidOperationException("Return not initiated");
        }
        
        // Refund logic here if needed
        order.setStatus(OrderStatus.RETURNED);
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order reviewOrder(String orderId, boolean isValid, String adminNotes) {
        Order order = getOrderById(Long.parseLong(orderId));
        order.setValid(isValid);
        order.setAdminNotes(adminNotes);
        
        if (!isValid) {
            order.setStatus(OrderStatus.REJECTED);
        }
        
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order approveOrder(String orderId, String adminSignature) {
        Order order = getOrderById(Long.parseLong(orderId));
        
        if (!order.isValid()) {
            throw new InvalidOperationException("Cannot approve invalid order");
        }
        
        order.setAdminApproved(true);
        order.setAdminSignature(adminSignature);
        order.setStatus(OrderStatus.APPROVED);
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order cancelOrder(String orderId, String reason) {
        Order order = getOrderById(Long.parseLong(orderId));
        order.setStatus(OrderStatus.CANCELLED);
        order.setAdminNotes(reason);
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public void updateEstimatedDeliveryDate(String orderId, LocalDateTime estimatedDate) {
        Order order = getOrderById(Long.parseLong(orderId));
        order.setEstimatedDeliveryDate(estimatedDate);
        orderRepository.save(order);
    }

    @Override
    public boolean isStockAvailable(String orderId) {
        Order order = getOrderById(Long.parseLong(orderId));
        return order.getOrderItems().stream()
            .allMatch(item -> {
                Product product = item.getProduct();
                return product.getStockQuantity() >= item.getQuantity();
            });
    }

    @Override
    @Transactional
    public void requestStockFromVendor(String orderId) {
        Order order = getOrderById(Long.parseLong(orderId));
        if (order.getStatus() != OrderStatus.STOCK_PENDING) {
            throw new InvalidOperationException("Order must be in STOCK_PENDING state");
        }
        // Implementation for vendor stock request would go here
        // This could involve calling a vendor service or sending notifications
    }

    @Override
    @Transactional
    public void updateStockLevel(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        product.setStockQuantity(quantity);
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void updateActualDeliveryDate(String orderId, LocalDateTime actualDate) {
        Order order = getOrderById(Long.parseLong(orderId));
        order.setActualDeliveryDate(actualDate);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order updateOrder(Long id, Order order) {
        Order existingOrder = getOrderById(id);
        // Update relevant fields
        existingOrder.setStatus(order.getStatus());
        existingOrder.setPaymentStatus(order.getPaymentStatus());
        existingOrder.setOrderItems(order.getOrderItems());
        return orderRepository.save(existingOrder);
    }
} 