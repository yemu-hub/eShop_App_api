package com.example.ecommerce.dto;

import lombok.Data;
import java.util.Date;

@Data
public class VendorDTO {
    private Long id;
    private String name;
    private String description;
    private String email;
    private String phone;
    private String website;
    private AddressDTO address;
    private String status;
    private Date registrationDate;
    private Double rating;
    private String taxId;
    private String businessLicense;
} 