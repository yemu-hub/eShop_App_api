package com.example.ecommerce.service;

import com.example.ecommerce.dto.LoginRequest;
import com.example.ecommerce.dto.UserDTO;
import com.example.ecommerce.dto.UserRegistrationRequest;

public interface AuthService {
    UserDTO register(UserRegistrationRequest request);
    UserDTO login(LoginRequest request);
    void verifyEmail(String token);
    void verifyMobile(String mobile, String otp);
    void resendVerificationEmail(String email);
    void resendMobileOtp(String mobile);
    void initiatePasswordReset(String email);
    void resetPassword(String token, String newPassword);
} 