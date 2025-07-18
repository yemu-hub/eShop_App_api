package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.SalesReportDTO;
import com.example.ecommerce.service.SalesReportService;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.CustomerRepository;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.Customer;
import com.example.ecommerce.model.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.ss.usermodel.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SalesReportServiceImpl implements SalesReportService {

    @Autowired private OrderRepository orderRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private CustomerRepository customerRepository;

    @Override
    public SalesReportDTO generateReport(LocalDate startDate, LocalDate endDate) {
        List<Order> orders = orderRepository.findByOrderDateBetween(startDate, endDate);
        
        SalesReportDTO report = new SalesReportDTO();
        report.setStartDate(startDate);
        report.setEndDate(endDate);
        
        // Calculate total revenue
        BigDecimal totalRevenue = orders.stream()
            .map(Order::getTotalAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        report.setTotalRevenue(totalRevenue);
        
        // Calculate average order value
        if (!orders.isEmpty()) {
            BigDecimal avgOrderValue = totalRevenue.divide(BigDecimal.valueOf(orders.size()));
            report.setAverageOrderValue(avgOrderValue);
        }
        
        // Calculate total number of orders
        report.setTotalOrders(orders.size());
        
        // Calculate sales by category
        Map<String, BigDecimal> salesByCategory = orders.stream()
            .flatMap(order -> order.getOrderItems().stream())
            .collect(Collectors.groupingBy(
                item -> item.getProduct().getCategory(),
                Collectors.reducing(
                    BigDecimal.ZERO,
                    OrderItem::getSubTotal,
                    BigDecimal::add
                )
            ));
        report.setSalesByCategory(salesByCategory);
        
        return report;
    }

    @Override
    public byte[] exportReport(LocalDate startDate, LocalDate endDate) {
        SalesReportDTO report = generateReport(startDate, endDate);
        
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            // Create Overview sheet
            XSSFSheet overviewSheet = workbook.createSheet("Overview");
            createOverviewSheet(overviewSheet, report);
            
            // Create Product Performance sheet
            XSSFSheet productSheet = workbook.createSheet("Product Performance");
            createProductPerformanceSheet(productSheet, report.getProductPerformance());
            
            // Create Customer Segments sheet
            XSSFSheet segmentSheet = workbook.createSheet("Customer Segments");
            createCustomerSegmentsSheet(segmentSheet, report.getCustomerSegments());
            
            // Export to byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate Excel report", e);
        }
    }

    private BigDecimal calculateTotalRevenue(List<Order> orders) {
        return orders.stream()
            .map(Order::getTotalAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateAverageOrderValue(List<Order> orders) {
        if (orders.isEmpty()) return BigDecimal.ZERO;
        return calculateTotalRevenue(orders)
            .divide(BigDecimal.valueOf(orders.size()), 2, RoundingMode.HALF_UP);
    }

    private double calculateGrowthPercentage(double previous, double current) {
        if (previous == 0) return 100.0;
        return ((current - previous) / previous) * 100.0;
    }

    private double calculateCustomerGrowth(
            LocalDate prevStart, LocalDate prevEnd, 
            LocalDate currStart, LocalDate currEnd) {
        int previousCustomers = customerRepository.countByCreatedAtBetween(prevStart, prevEnd);
        int currentCustomers = customerRepository.countByCreatedAtBetween(currStart, currEnd);
        return calculateGrowthPercentage(previousCustomers, currentCustomers);
    }

    private List<SalesReportDTO.RevenueDataPoint> generateRevenueData(LocalDate start, LocalDate end) {
        return orderRepository.findDailyRevenue(start, end).stream()
            .map(data -> {
                SalesReportDTO.RevenueDataPoint point = new SalesReportDTO.RevenueDataPoint();
                point.setDate(data.getDate().toString());
                point.setAmount(data.getAmount());
                return point;
            })
            .collect(Collectors.toList());
    }

    private List<SalesReportDTO.CategorySalesData> generateCategorySalesData(List<Order> orders) {
        Map<String, BigDecimal> categorySales = new HashMap<>();
        
        orders.forEach(order -> {
            order.getItems().forEach(item -> {
                String category = item.getProduct().getCategory();
                BigDecimal amount = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                categorySales.merge(category, amount, BigDecimal::add);
            });
        });
        
        return categorySales.entrySet().stream()
            .map(entry -> {
                SalesReportDTO.CategorySalesData data = new SalesReportDTO.CategorySalesData();
                data.setCategory(entry.getKey());
                data.setSales(entry.getValue());
                return data;
            })
            .collect(Collectors.toList());
    }

    private List<SalesReportDTO.CustomerSegment> generateCustomerSegments(List<Order> orders) {
        // Group customers by their order frequency and total spend
        Map<Long, List<Order>> customerOrders = orders.stream()
            .collect(Collectors.groupingBy(order -> order.getCustomer().getId()));
        
        List<SalesReportDTO.CustomerSegment> segments = new ArrayList<>();
        
        // Loyal Customers (>= 3 orders)
        SalesReportDTO.CustomerSegment loyal = new SalesReportDTO.CustomerSegment();
        loyal.setName("Loyal Customers");
        
        // Regular Customers (2 orders)
        SalesReportDTO.CustomerSegment regular = new SalesReportDTO.CustomerSegment();
        regular.setName("Regular Customers");
        
        // New Customers (1 order)
        SalesReportDTO.CustomerSegment newCustomers = new SalesReportDTO.CustomerSegment();
        newCustomers.setName("New Customers");
        
        customerOrders.forEach((customerId, customerOrderList) -> {
            SalesReportDTO.CustomerSegment segment;
            if (customerOrderList.size() >= 3) {
                segment = loyal;
            } else if (customerOrderList.size() == 2) {
                segment = regular;
            } else {
                segment = newCustomers;
            }
            
            updateSegmentMetrics(segment, customerOrderList);
        });
        
        segments.add(loyal);
        segments.add(regular);
        segments.add(newCustomers);
        
        return segments;
    }

    private void updateSegmentMetrics(SalesReportDTO.CustomerSegment segment, List<Order> orders) {
        segment.setCustomerCount(segment.getCustomerCount() + 1);
        BigDecimal revenue = calculateTotalRevenue(orders);
        segment.setRevenue(segment.getRevenue().add(revenue));
        segment.setAvgOrderValue(segment.getRevenue()
            .divide(BigDecimal.valueOf(segment.getCustomerCount()), 2, RoundingMode.HALF_UP));
    }

    private List<SalesReportDTO.ProductPerformance> generateProductPerformance(List<Order> orders) {
        Map<Long, SalesReportDTO.ProductPerformance> performance = new HashMap<>();
        
        orders.forEach(order -> {
            order.getItems().forEach(item -> {
                Product product = item.getProduct();
                performance.computeIfAbsent(product.getId(), k -> {
                    SalesReportDTO.ProductPerformance pp = new SalesReportDTO.ProductPerformance();
                    pp.setId(product.getId());
                    pp.setName(product.getName());
                    pp.setCategory(product.getCategory());
                    pp.setStockLevel(product.getStockQuantity());
                    return pp;
                });
                
                SalesReportDTO.ProductPerformance pp = performance.get(product.getId());
                pp.setUnitsSold(pp.getUnitsSold() + item.getQuantity());
                pp.setRevenue(pp.getRevenue().add(
                    item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
                ));
                pp.setProfitMargin(calculateProfitMargin(item));
            });
        });
        
        return new ArrayList<>(performance.values());
    }

    private double calculateProfitMargin(OrderItem item) {
        BigDecimal cost = item.getProduct().getCost();
        BigDecimal price = item.getPrice();
        return price.subtract(cost)
            .divide(price, 4, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100))
            .doubleValue();
    }

    private List<String> generateInventoryRecommendations() {
        List<String> recommendations = new ArrayList<>();
        List<Product> lowStock = productRepository.findByStockQuantityLessThan(10);
        List<Product> highStock = productRepository.findByStockQuantityGreaterThan(100);
        
        lowStock.forEach(product -> 
            recommendations.add("Low stock alert: " + product.getName() + " (Only " + product.getStockQuantity() + " left)")
        );
        
        highStock.forEach(product -> 
            recommendations.add("Consider reducing stock for: " + product.getName() + " (Current stock: " + product.getStockQuantity() + ")")
        );
        
        return recommendations;
    }

    private List<String> generatePriceRecommendations() {
        List<String> recommendations = new ArrayList<>();
        List<Product> products = productRepository.findAll();
        
        products.forEach(product -> {
            double margin = calculateProductMargin(product);
            if (margin < 20) {
                recommendations.add(
                    "Consider increasing price for " + product.getName() + 
                    " (Current margin: " + String.format("%.2f%%", margin) + ")"
                );
            }
        });
        
        return recommendations;
    }

    private double calculateProductMargin(Product product) {
        return product.getPrice().subtract(product.getCost())
            .divide(product.getPrice(), 4, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100))
            .doubleValue();
    }

    private void createOverviewSheet(XSSFSheet sheet, SalesReportDTO report) {
        // Implementation for creating overview sheet
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Metric");
        headerRow.createCell(1).setCellValue("Value");
        
        Row revenueRow = sheet.createRow(1);
        revenueRow.createCell(0).setCellValue("Total Revenue");
        revenueRow.createCell(1).setCellValue(report.getTotalRevenue().doubleValue());
        
        // Add more rows for other metrics...
    }

    private void createProductPerformanceSheet(XSSFSheet sheet, List<SalesReportDTO.ProductPerformance> products) {
        // Implementation for creating product performance sheet
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Product");
        headerRow.createCell(1).setCellValue("Category");
        headerRow.createCell(2).setCellValue("Units Sold");
        headerRow.createCell(3).setCellValue("Revenue");
        headerRow.createCell(4).setCellValue("Profit Margin");
        
        // Add product rows...
    }

    private void createCustomerSegmentsSheet(XSSFSheet sheet, List<SalesReportDTO.CustomerSegment> segments) {
        // Implementation for creating customer segments sheet
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Segment");
        headerRow.createCell(1).setCellValue("Customers");
        headerRow.createCell(2).setCellValue("Revenue");
        headerRow.createCell(3).setCellValue("Avg Order Value");
        
        // Add segment rows...
    }
} 