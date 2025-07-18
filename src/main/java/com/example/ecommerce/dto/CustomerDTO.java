package com.example.ecommerce.dto;

import lombok.Data;
import java.util.Date;

@Data
public class CustomerDTO {
    private Long id;
    private String userName;
    private String email;
    private String add;
    private String phone;
    private String IPAdd;
    private Integer nClicks;
    private Date timestamp;
} 