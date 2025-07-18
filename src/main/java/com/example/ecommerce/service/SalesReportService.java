package com.example.ecommerce.service;

import com.example.ecommerce.dto.SalesReportDTO;
import java.time.LocalDate;

public interface SalesReportService {
    SalesReportDTO generateReport(LocalDate startDate, LocalDate endDate);
    byte[] exportReport(LocalDate startDate, LocalDate endDate);
} 