package com.example.ecommerce.service.impl;

import com.example.ecommerce.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Override
    public void sendVerificationEmail(String email, String token) {
        // In a real implementation, this would send an actual email
        // For development, we'll just log the email details
        logger.info("Sending verification email to: {}", email);
        logger.info("Verification link: http://localhost:8085/api/auth/verify-email/{}", token);
    }

    @Override
    public void sendPasswordResetEmail(String email, String token) {
        // In a real implementation, this would send an actual email
        // For development, we'll just log the email details
        logger.info("Sending password reset email to: {}", email);
        logger.info("Password reset link: http://localhost:8085/api/auth/reset-password?token={}", token);
    }
} 