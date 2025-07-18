package com.example.ecommerce.service;

public interface OtpService {
    String generateOtp(String mobile);
    void sendOtp(String mobile, String otp);
    boolean validateOtp(String mobile, String otp);
} 