package com.example.ecommerce.dto;

import lombok.Data;

@Data
public class AddressDTO {
    private Long id;
    private String streetAddress;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String addressType;
    private boolean isDefault;
    private String additionalInfo;
} 