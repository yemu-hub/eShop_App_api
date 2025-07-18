package com.example.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "catalogs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Catalog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    private String name;

    private Integer importance;

    @OneToMany(mappedBy = "catalog")
    private List<Product> products = new ArrayList<>();

    private String description;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private boolean active = true;

    @OneToMany
    private List<Offer> offers = new ArrayList<>();

    @OneToMany
    private List<Coupon> coupons = new ArrayList<>();

    private String category;

    private String season;

    @PrePersist
    public void prePersist() {
        if (startDate == null) {
            startDate = LocalDateTime.now();
        }
    }

    // Explicit getter and setter methods for startDate
    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    // Explicit getter and setter methods for endDate
    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    // Explicit getter and setter methods for active
    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }
} 