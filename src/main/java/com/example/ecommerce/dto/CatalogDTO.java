package com.example.ecommerce.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CatalogDTO {
    private Integer id;

    @NotBlank(message = "Name is required")
    private String name;

    private String description;
    private String category;
    private String season;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean active = true;
    private Integer importance;
} 