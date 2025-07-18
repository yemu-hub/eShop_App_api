package com.example.ecommerce.dto;

import lombok.Data;
import java.util.Date;

@Data
public class PaymentDTO {
    private Integer paymentNumber;
    private Double amount;
    private String state;
    private Date timestamp;
} 