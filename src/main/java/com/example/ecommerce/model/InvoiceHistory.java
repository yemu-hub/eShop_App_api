package com.example.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Entity
@Table(name = "invoice_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer invStateNumber;

    private String stateDesc;
    private String notes;
    private Date timestamp;

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;
} 