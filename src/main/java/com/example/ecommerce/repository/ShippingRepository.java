package com.example.ecommerce.repository;

import com.example.ecommerce.model.Shipping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ShippingRepository extends JpaRepository<Shipping, Long> {
    List<Shipping> findByStatus(String status);
    List<Shipping> findByMethod(String method);
    List<Shipping> findByOrderId(Long orderId);
    List<Shipping> findByTrackingNumber(String trackingNumber);
    List<Shipping> findByShippingCountry(String country);
    List<Shipping> findByShippingState(String state);
    List<Shipping> findByStatusAndMethod(String status, String method);
} 