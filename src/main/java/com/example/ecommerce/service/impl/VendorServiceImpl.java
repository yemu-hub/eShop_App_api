package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.VendorDTO;
import com.example.ecommerce.model.Vendor;
import com.example.ecommerce.repository.VendorRepository;
import com.example.ecommerce.service.VendorService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

@Service
public class VendorServiceImpl implements VendorService {

    @Autowired
    private VendorRepository vendorRepository;

    @Override
    public List<VendorDTO> getAllVendors() {
        return vendorRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public VendorDTO getVendorById(Long id) {
        Vendor vendor = vendorRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Vendor not found with id: " + id));
        return convertToDTO(vendor);
    }

    @Override
    public VendorDTO createVendor(VendorDTO vendorDTO) {
        if (vendorRepository.existsByEmail(vendorDTO.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }
        Vendor vendor = convertToEntity(vendorDTO);
        vendor.setRegistrationDate(LocalDateTime.now());
        vendor.setStatus("PENDING");
        return convertToDTO(vendorRepository.save(vendor));
    }

    @Override
    public VendorDTO updateVendor(Long id, VendorDTO vendorDTO) {
        Vendor vendor = vendorRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Vendor not found with id: " + id));
        
        if (!vendor.getEmail().equals(vendorDTO.getEmail()) && 
            vendorRepository.existsByEmail(vendorDTO.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }
        
        updateVendorFromDTO(vendor, vendorDTO);
        return convertToDTO(vendorRepository.save(vendor));
    }

    @Override
    public void deleteVendor(Long id) {
        if (!vendorRepository.existsById(id)) {
            throw new EntityNotFoundException("Vendor not found with id: " + id);
        }
        vendorRepository.deleteById(id);
    }

    @Override
    public VendorDTO updateVendorStatus(Long id, String status) {
        Vendor vendor = vendorRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Vendor not found with id: " + id));
        vendor.setStatus(status);
        return convertToDTO(vendorRepository.save(vendor));
    }

    private VendorDTO convertToDTO(Vendor vendor) {
        VendorDTO dto = new VendorDTO();
        BeanUtils.copyProperties(vendor, dto);
        return dto;
    }

    private Vendor convertToEntity(VendorDTO dto) {
        Vendor vendor = new Vendor();
        BeanUtils.copyProperties(dto, vendor);
        return vendor;
    }

    private void updateVendorFromDTO(Vendor vendor, VendorDTO dto) {
        String originalEmail = vendor.getEmail();
        LocalDateTime originalRegDate = vendor.getRegistrationDate();
        Long originalId = vendor.getId();
        
        BeanUtils.copyProperties(dto, vendor);
        
        // Restore the fields that shouldn't be updated
        vendor.setId(originalId);
        vendor.setEmail(originalEmail);
        vendor.setRegistrationDate(originalRegDate);
    }
} 