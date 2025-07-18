package com.example.ecommerce.repository;

import com.example.ecommerce.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByEmail(String email);
    boolean existsByEmail(String email);
    @Query("SELECT COUNT(c) FROM Customer c WHERE DATE(c.timestamp) BETWEEN :startDate AND :endDate")
    int countByCreatedAtBetween(LocalDate startDate, LocalDate endDate);
} 