package com.example.ecommerce.dto;

import lombok.Data;
import java.util.Date;

@Data
public class InventoryDTO {
    private Integer id;
    private Integer productId;
    private Integer quantity;
    private String location;
    private String status;
    private Date lastUpdated;
    private Integer minStockLevel;
    private Integer maxStockLevel;
    private String notes;
} 