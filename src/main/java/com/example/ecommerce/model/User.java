package com.example.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Column(name = "mobile")
    private String mobile;
    
    @Column(name = "email_verified")
    private boolean emailVerified;
    
    @Column(name = "mobile_verified")
    private boolean mobileVerified;
    
    @Column(name = "email_verification_token")
    private String emailVerificationToken;
    
    @Column(name = "password_reset_token")
    private String passwordResetToken;
    
    @Column(name = "password_reset_expiry")
    private LocalDateTime passwordResetExpiry;
    
    @Column(name = "last_login")
    private LocalDateTime lastLogin;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;
} 