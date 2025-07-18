package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.LoginRequest;
import com.example.ecommerce.dto.UserDTO;
import com.example.ecommerce.dto.UserRegistrationRequest;
import com.example.ecommerce.model.User;
import com.example.ecommerce.model.UserRole;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.service.AuthService;
import com.example.ecommerce.service.EmailService;
import com.example.ecommerce.service.OtpService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OtpService otpService;

    @Override
    @Transactional
    public UserDTO register(UserRegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        if (userRepository.existsByMobile(request.getMobile())) {
            throw new IllegalArgumentException("Mobile number already registered");
        }

        User user = new User();
        BeanUtils.copyProperties(request, user);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.CUSTOMER);
        user.setEmailVerified(false);
        user.setMobileVerified(false);
        
        User savedUser = userRepository.save(user);
        
        // Send verification email
        String emailToken = UUID.randomUUID().toString();
        emailService.sendVerificationEmail(user.getEmail(), emailToken);
        
        // Send mobile OTP
        String otp = otpService.generateOtp(user.getMobile());
        otpService.sendOtp(user.getMobile(), otp);

        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(savedUser, userDTO);
        return userDTO;
    }

    @Override
    public UserDTO login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        if (!user.isEmailVerified()) {
            throw new IllegalStateException("Email not verified");
        }

        if (!user.isMobileVerified()) {
            throw new IllegalStateException("Mobile number not verified");
        }

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }

    @Override
    @Transactional
    public void verifyEmail(String token) {
        User user = userRepository.findByEmailVerificationToken(token)
            .orElseThrow(() -> new EntityNotFoundException("Invalid verification token"));

        user.setEmailVerified(true);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void verifyMobile(String mobile, String otp) {
        if (!otpService.validateOtp(mobile, otp)) {
            throw new IllegalArgumentException("Invalid OTP");
        }

        User user = userRepository.findByMobile(mobile)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setMobileVerified(true);
        userRepository.save(user);
    }

    @Override
    public void resendVerificationEmail(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (user.isEmailVerified()) {
            throw new IllegalStateException("Email already verified");
        }

        String emailToken = UUID.randomUUID().toString();
        emailService.sendVerificationEmail(user.getEmail(), emailToken);
    }

    @Override
    public void resendMobileOtp(String mobile) {
        User user = userRepository.findByMobile(mobile)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (user.isMobileVerified()) {
            throw new IllegalStateException("Mobile number already verified");
        }

        String otp = otpService.generateOtp(mobile);
        otpService.sendOtp(mobile, otp);
    }

    @Override
    public void initiatePasswordReset(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));

        String resetToken = UUID.randomUUID().toString();
        user.setPasswordResetToken(resetToken);
        user.setPasswordResetExpiry(LocalDateTime.now().plusHours(24));
        userRepository.save(user);

        emailService.sendPasswordResetEmail(email, resetToken);
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByPasswordResetToken(token)
            .orElseThrow(() -> new EntityNotFoundException("Invalid reset token"));

        if (user.getPasswordResetExpiry().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Reset token expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordResetToken(null);
        user.setPasswordResetExpiry(null);
        userRepository.save(user);
    }
} 