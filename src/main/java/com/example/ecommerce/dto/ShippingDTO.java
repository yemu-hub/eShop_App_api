package com.example.ecommerce.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ShippingDTO {
    private Long id;
    private Long orderId;
    private String status;
    private String method;
    private String trackingNumber;
    private String shippingAddress;
    private String shippingCity;
    private String shippingState;
    private String shippingCountry;
    private String shippingZip;
    private String shippingPhone;
    private String shippingEmail;
    private String shippingInstructions;
    private LocalDateTime estimatedDeliveryDate;
    private LocalDateTime actualDeliveryDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 