package com.example.ecommerce.dto;

import com.example.ecommerce.model.UserRole;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String mobile;
    private AddressDTO address;
    private UserRole role;
    private boolean active;
    private boolean emailVerified;
    private boolean mobileVerified;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 