package com.example.ecommerce.controller;

import com.example.ecommerce.dto.*;
import com.example.ecommerce.service.*;
import com.example.ecommerce.model.*;
import com.example.ecommerce.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.time.LocalDate;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired private OrderService orderService;
    @Autowired private CustomerService customerService;
    @Autowired private VendorService vendorService;
    @Autowired private ProductService productService;
    @Autowired private AuthService authService;
    @Autowired private OrderMapper orderMapper;
    @Autowired private ProductMapper productMapper;
    @Autowired private SalesReportService salesReportService;

    @GetMapping("/login")
    public String loginPage() {
        return "admin/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Get pending orders
        List<Order> pendingOrders = orderService.getPendingOrders();
        model.addAttribute("pendingOrders", pendingOrders.stream()
            .map(orderMapper::toDTO)
            .toList());
        
        // Get customer statistics
        List<CustomerDTO> recentCustomers = customerService.getAllCustomers();
        model.addAttribute("recentCustomers", recentCustomers);
        
        // Get vendor information
        List<VendorDTO> activeVendors = vendorService.getAllVendors();
        model.addAttribute("activeVendors", activeVendors);
        
        return "admin/dashboard";
    }

    @GetMapping("/sales-report")
    public String salesReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            Model model) {
        
        // If dates not provided, use last 30 days
        if (start == null) {
            start = LocalDate.now().minusDays(30);
        }
        if (end == null) {
            end = LocalDate.now();
        }

        // Get sales report data
        SalesReportDTO report = salesReportService.generateReport(start, end);
        
        // Add all report data to model
        model.addAttribute("totalRevenue", report.getTotalRevenue());
        model.addAttribute("totalOrders", report.getTotalOrders());
        model.addAttribute("avgOrderValue", report.getAverageOrderValue());
        model.addAttribute("newCustomers", report.getNewCustomers());
        
        model.addAttribute("revenueGrowth", report.getRevenueGrowth());
        model.addAttribute("orderGrowth", report.getOrderGrowth());
        model.addAttribute("avgOrderGrowth", report.getAverageOrderGrowth());
        model.addAttribute("customerGrowth", report.getCustomerGrowth());
        
        model.addAttribute("revenueData", report.getRevenueData());
        model.addAttribute("categoryData", report.getCategoryData());
        model.addAttribute("customerSegments", report.getCustomerSegments());
        model.addAttribute("productPerformance", report.getProductPerformance());
        
        model.addAttribute("inventoryRecommendations", report.getInventoryRecommendations());
        model.addAttribute("priceRecommendations", report.getPriceRecommendations());
        
        return "admin/sales-report";
    }

    @GetMapping("/sales-report/export")
    public ResponseEntity<byte[]> exportSalesReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        
        if (start == null) {
            start = LocalDate.now().minusDays(30);
        }
        if (end == null) {
            end = LocalDate.now();
        }

        byte[] report = salesReportService.exportReport(start, end);
        
        return ResponseEntity.ok()
                .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .header("Content-Disposition", "attachment; filename=sales-report.xlsx")
                .body(report);
    }

    @GetMapping("/b2b")
    public String b2bManagement(Model model) {
        List<VendorDTO> vendors = vendorService.getAllVendors();
        model.addAttribute("vendors", vendors);
        return "admin/b2b";
    }

    @GetMapping("/customers")
    public String customerManagement(Model model) {
        List<CustomerDTO> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);
        return "admin/customers";
    }

    @GetMapping("/products")
    public String productCatalog(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long edit,
            Model model) {
        List<Product> products;
        if (search != null && !search.isEmpty()) {
            products = productService.searchProducts(search);
        } else {
            products = productService.getAllProducts();
        }
        
        model.addAttribute("products", products.stream()
            .map(productMapper::toDTO)
            .toList());
        
        // Add categories for product forms
        model.addAttribute("categories", productService.getAllCategories());
        
        // If edit parameter is present, set flag to open edit modal
        if (edit != null) {
            model.addAttribute("editProductId", edit);
        }
        
        return "admin/products";
    }

    @GetMapping("/account")
    public String accountSettings(Model model) {
        return "admin/account";
    }

    @PostMapping("/products/add")
    public String addProduct(@ModelAttribute ProductDTO productDTO) {
        Product product = productMapper.toEntity(productDTO);
        productService.createProduct(product);
        return "redirect:/admin/products";
    }

    @PostMapping("/products/{id}/edit")
    public String editProduct(@PathVariable Long id, @ModelAttribute ProductDTO productDTO) {
        Product product = productMapper.toEntity(productDTO);
        productService.updateProduct(id, product);
        return "redirect:/admin/products";
    }

    @PostMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }

    @PutMapping("/products/{id}/status")
    @ResponseBody
    public ResponseEntity<Void> updateProductStatus(@PathVariable Long id, @RequestBody Map<String, Boolean> status) {
        productService.updateProductStatus(id, status.get("active"));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/orders/{id}/approve")
    public String approveOrder(@PathVariable String id, @RequestParam String adminSignature) {
        orderService.approveOrder(id, adminSignature);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/orders/{id}/reject")
    public String rejectOrder(@PathVariable String id, @RequestParam String reason) {
        orderService.cancelOrder(id, reason);
        return "redirect:/admin/dashboard";
    }
} 