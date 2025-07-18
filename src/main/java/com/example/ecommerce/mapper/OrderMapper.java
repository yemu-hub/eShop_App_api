package com.example.ecommerce.mapper;

import com.example.ecommerce.dto.OrderDTO;
import com.example.ecommerce.dto.OrderItemDTO;
import com.example.ecommerce.model.*;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.CustomerRepository;
import com.example.ecommerce.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderMapper {
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    public OrderDTO toDTO(Order order) {
        if (order == null) {
            return null;
        }

        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setCustomerId(order.getCustomer().getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus().name());
        
        dto.setItems(order.getOrderItems().stream()
            .map(this::toDTO)
            .collect(Collectors.toList()));
            
        return dto;
    }

    public OrderItemDTO toDTO(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }

        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(orderItem.getId());
        dto.setProductId(orderItem.getProduct().getId());
        dto.setProductName(orderItem.getProduct().getName());
        dto.setQuantity(orderItem.getQuantity());
        dto.setPrice(orderItem.getPrice());
        dto.setSubTotal(orderItem.getSubTotal());
        
        return dto;
    }

    public List<OrderItem> toOrderItems(List<OrderItemDTO> dtos) {
        return dtos.stream()
            .map(this::toOrderItem)
            .collect(Collectors.toList());
    }

    public OrderItem toOrderItem(OrderItemDTO dto) {
        if (dto == null) {
            return null;
        }

        OrderItem orderItem = new OrderItem();
        if (dto.getId() != null) {
            orderItem.setId(dto.getId());
        }
        
        Product product = productRepository.findById(dto.getProductId())
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + dto.getProductId()));
        
        orderItem.setProduct(product);
        orderItem.setQuantity(dto.getQuantity());
        orderItem.setPrice(dto.getPrice());
        
        return orderItem;
    }

    public Order toEntity(OrderDTO dto) {
        if (dto == null) {
            return null;
        }

        Order order = new Order();
        order.setId(dto.getId());
        order.setOrderDate(dto.getOrderDate());
        order.setTotalAmount(dto.getTotalAmount());
        order.setStatus(OrderStatus.valueOf(dto.getStatus()));
        
        if (dto.getItems() != null) {
            order.setOrderItems(dto.getItems().stream()
                .map(this::toEntity)
                .collect(Collectors.toList()));
        }
        
        return order;
    }

    public List<OrderDTO> toDTOList(List<Order> orders) {
        return orders.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public OrderItem toEntity(OrderItemDTO dto) {
        if (dto == null) {
            return null;
        }

        OrderItem orderItem = new OrderItem();
        orderItem.setId(dto.getId());
        orderItem.setQuantity(dto.getQuantity());
        orderItem.setPrice(dto.getPrice());
        
        return orderItem;
    }
}