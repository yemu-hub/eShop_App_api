package com.example.ecommerce.service;

import com.example.ecommerce.dto.VendorDTO;
import java.util.List;

public interface VendorService {
    List<VendorDTO> getAllVendors();
    VendorDTO getVendorById(Long id);
    VendorDTO createVendor(VendorDTO vendorDTO);
    VendorDTO updateVendor(Long id, VendorDTO vendorDTO);
    void deleteVendor(Long id);
    VendorDTO updateVendorStatus(Long id, String status);
}