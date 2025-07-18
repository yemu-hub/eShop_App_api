package com.example.ecommerce.repository;

import com.example.ecommerce.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
    List<Vendor> findByStatus(String status);
    List<Vendor> findByRatingGreaterThanEqual(Double rating);
    boolean existsByEmail(String email);
    List<Vendor> findByNameContainingIgnoreCase(String name);
} 