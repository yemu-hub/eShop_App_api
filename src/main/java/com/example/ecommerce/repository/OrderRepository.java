package com.example.ecommerce.repository;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerId(Long customerId);
    List<Order> findByStatus(OrderStatus status);
    List<Order> findByOrderDateBetween(LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT new map(o.orderDate as date, SUM(o.totalAmount) as amount) " +
           "FROM Order o " +
           "WHERE o.orderDate BETWEEN :startDate AND :endDate " +
           "GROUP BY o.orderDate " +
           "ORDER BY o.orderDate")
    List<DailyRevenue> findDailyRevenue(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    interface DailyRevenue {
        LocalDate getDate();
        BigDecimal getAmount();
    }
} 