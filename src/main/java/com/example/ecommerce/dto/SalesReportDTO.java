package com.example.ecommerce.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.List;

@Data
public class SalesReportDTO {
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalRevenue;
    private BigDecimal averageOrderValue;
    private int totalOrders;
    private Map<String, BigDecimal> salesByCategory;
    
    private int newCustomers;
    
    private double revenueGrowth;
    private double orderGrowth;
    private double averageOrderGrowth;
    private double customerGrowth;
    
    private List<RevenueDataPoint> revenueData;
    private List<CategorySalesData> categoryData;
    private List<CustomerSegment> customerSegments;
    private List<ProductPerformance> productPerformance;
    
    private List<String> inventoryRecommendations;
    private List<String> priceRecommendations;
    
    @Data
    public static class RevenueDataPoint {
        private String date;
        private BigDecimal amount;
    }
    
    @Data
    public static class CategorySalesData {
        private String category;
        private BigDecimal sales;
    }
    
    @Data
    public static class CustomerSegment {
        private String name;
        private int customerCount;
        private BigDecimal revenue;
        private BigDecimal avgOrderValue;
    }
    
    @Data
    public static class ProductPerformance {
        private Long id;
        private String name;
        private String category;
        private int unitsSold;
        private BigDecimal revenue;
        private double profitMargin;
        private int stockLevel;
    }
} 