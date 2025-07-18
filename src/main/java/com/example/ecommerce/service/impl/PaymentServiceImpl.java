package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.PaymentDTO;
import com.example.ecommerce.model.Payment;
import com.example.ecommerce.repository.PaymentRepository;
import com.example.ecommerce.service.PaymentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public PaymentDTO createPayment(PaymentDTO paymentDTO) {
        Payment payment = new Payment();
        BeanUtils.copyProperties(paymentDTO, payment);
        Payment savedPayment = paymentRepository.save(payment);
        PaymentDTO savedDTO = new PaymentDTO();
        BeanUtils.copyProperties(savedPayment, savedDTO);
        return savedDTO;
    }

    @Override
    public PaymentDTO getPaymentById(Integer id) {
        Payment payment = paymentRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Payment not found with id: " + id));
        PaymentDTO paymentDTO = new PaymentDTO();
        BeanUtils.copyProperties(payment, paymentDTO);
        return paymentDTO;
    }

    @Override
    public List<PaymentDTO> getAllPayments() {
        return paymentRepository.findAll().stream()
            .map(payment -> {
                PaymentDTO dto = new PaymentDTO();
                BeanUtils.copyProperties(payment, dto);
                return dto;
            })
            .collect(Collectors.toList());
    }

    @Override
    public List<PaymentDTO> getPaymentsByState(String state) {
        return paymentRepository.findByState(state).stream()
            .map(payment -> {
                PaymentDTO dto = new PaymentDTO();
                BeanUtils.copyProperties(payment, dto);
                return dto;
            })
            .collect(Collectors.toList());
    }

    @Override
    public PaymentDTO updatePayment(Integer id, PaymentDTO paymentDTO) {
        Payment payment = paymentRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Payment not found with id: " + id));
        BeanUtils.copyProperties(paymentDTO, payment, "id");
        Payment updatedPayment = paymentRepository.save(payment);
        PaymentDTO updatedDTO = new PaymentDTO();
        BeanUtils.copyProperties(updatedPayment, updatedDTO);
        return updatedDTO;
    }

    @Override
    public void deletePayment(Integer id) {
        if (!paymentRepository.existsById(id)) {
            throw new EntityNotFoundException("Payment not found with id: " + id);
        }
        paymentRepository.deleteById(id);
    }

    @Override
    public PaymentDTO updatePaymentState(Integer id, String state) {
        Payment payment = paymentRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Payment not found with id: " + id));
        payment.setState(state);
        Payment updatedPayment = paymentRepository.save(payment);
        PaymentDTO updatedDTO = new PaymentDTO();
        BeanUtils.copyProperties(updatedPayment, updatedDTO);
        return updatedDTO;
    }
} 