package com.example.ecommerce.service.impl;

import com.example.ecommerce.service.OtpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpServiceImpl implements OtpService {
    private static final Logger logger = LoggerFactory.getLogger(OtpServiceImpl.class);
    private static final Map<String, String> otpStore = new ConcurrentHashMap<>();
    private static final Random random = new Random();

    @Override
    public String generateOtp(String mobile) {
        // Generate a 6-digit OTP
        String otp = String.format("%06d", random.nextInt(1000000));
        otpStore.put(mobile, otp);
        return otp;
    }

    @Override
    public void sendOtp(String mobile, String otp) {
        // In a real implementation, this would send an actual SMS
        // For development, we'll just log the OTP
        logger.info("Sending OTP to mobile: {}", mobile);
        logger.info("OTP: {}", otp);
    }

    @Override
    public boolean validateOtp(String mobile, String otp) {
        String storedOtp = otpStore.get(mobile);
        if (storedOtp != null && storedOtp.equals(otp)) {
            otpStore.remove(mobile); // Remove OTP after successful validation
            return true;
        }
        return false;
    }
} 