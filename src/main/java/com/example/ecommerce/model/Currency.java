package com.example.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "currencies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String code; // USD, EUR, etc.

    @NotBlank
    private String name;

    @NotBlank
    private String symbol;

    private String description;

    private boolean isActive = true;

    private Double exchangeRate; // Exchange rate relative to base currency
} 