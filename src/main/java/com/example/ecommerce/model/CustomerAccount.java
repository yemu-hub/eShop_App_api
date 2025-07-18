package com.example.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Entity
@Table(name = "customer_accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String assNumber;
    private String accountId;
    private Date timestamp;
    private String PW;
    private Integer numVisits;
    private Integer numTrans;
    private Double ttlAmount;

    @OneToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
} 