package com.example.ecommerce.service;

import com.example.ecommerce.dto.PaymentDTO;
import java.util.List;

public interface PaymentService {
    PaymentDTO createPayment(PaymentDTO paymentDTO);
    PaymentDTO getPaymentById(Integer id);
    List<PaymentDTO> getAllPayments();
    List<PaymentDTO> getPaymentsByState(String state);
    PaymentDTO updatePayment(Integer id, PaymentDTO paymentDTO);
    void deletePayment(Integer id);
    PaymentDTO updatePaymentState(Integer id, String state);
} 