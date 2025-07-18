package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.ShippingDTO;
import com.example.ecommerce.model.Shipping;
import com.example.ecommerce.repository.ShippingRepository;
import com.example.ecommerce.service.ShippingService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShippingServiceImpl implements ShippingService {

    @Autowired
    private ShippingRepository shippingRepository;

    @Override
    public ShippingDTO createShipping(ShippingDTO shippingDTO) {
        Shipping shipping = new Shipping();
        BeanUtils.copyProperties(shippingDTO, shipping);
        Shipping savedShipping = shippingRepository.save(shipping);
        ShippingDTO savedDTO = new ShippingDTO();
        BeanUtils.copyProperties(savedShipping, savedDTO);
        return savedDTO;
    }

    @Override
    public ShippingDTO getShippingById(Long id) {
        Shipping shipping = shippingRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Shipping not found with id: " + id));
        ShippingDTO shippingDTO = new ShippingDTO();
        BeanUtils.copyProperties(shipping, shippingDTO);
        return shippingDTO;
    }

    @Override
    public List<ShippingDTO> getAllShippings() {
        return shippingRepository.findAll().stream()
            .map(shipping -> {
                ShippingDTO dto = new ShippingDTO();
                BeanUtils.copyProperties(shipping, dto);
                return dto;
            })
            .collect(Collectors.toList());
    }

    @Override
    public List<ShippingDTO> getShippingsByStatus(String status) {
        return shippingRepository.findByStatus(status).stream()
            .map(shipping -> {
                ShippingDTO dto = new ShippingDTO();
                BeanUtils.copyProperties(shipping, dto);
                return dto;
            })
            .collect(Collectors.toList());
    }

    @Override
    public List<ShippingDTO> getShippingsByMethod(String method) {
        return shippingRepository.findByMethod(method).stream()
            .map(shipping -> {
                ShippingDTO dto = new ShippingDTO();
                BeanUtils.copyProperties(shipping, dto);
                return dto;
            })
            .collect(Collectors.toList());
    }

    @Override
    public List<ShippingDTO> getShippingsByOrder(Long orderId) {
        return shippingRepository.findByOrderId(orderId).stream()
            .map(shipping -> {
                ShippingDTO dto = new ShippingDTO();
                BeanUtils.copyProperties(shipping, dto);
                return dto;
            })
            .collect(Collectors.toList());
    }

    @Override
    public List<ShippingDTO> getShippingsByTrackingNumber(String trackingNumber) {
        return shippingRepository.findByTrackingNumber(trackingNumber).stream()
            .map(shipping -> {
                ShippingDTO dto = new ShippingDTO();
                BeanUtils.copyProperties(shipping, dto);
                return dto;
            })
            .collect(Collectors.toList());
    }

    @Override
    public List<ShippingDTO> getShippingsByCountry(String country) {
        return shippingRepository.findByShippingCountry(country).stream()
            .map(shipping -> {
                ShippingDTO dto = new ShippingDTO();
                BeanUtils.copyProperties(shipping, dto);
                return dto;
            })
            .collect(Collectors.toList());
    }

    @Override
    public List<ShippingDTO> getShippingsByState(String state) {
        return shippingRepository.findByShippingState(state).stream()
            .map(shipping -> {
                ShippingDTO dto = new ShippingDTO();
                BeanUtils.copyProperties(shipping, dto);
                return dto;
            })
            .collect(Collectors.toList());
    }

    @Override
    public List<ShippingDTO> getShippingsByStatusAndMethod(String status, String method) {
        return shippingRepository.findByStatusAndMethod(status, method).stream()
            .map(shipping -> {
                ShippingDTO dto = new ShippingDTO();
                BeanUtils.copyProperties(shipping, dto);
                return dto;
            })
            .collect(Collectors.toList());
    }

    @Override
    public ShippingDTO updateShipping(Long id, ShippingDTO shippingDTO) {
        Shipping shipping = shippingRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Shipping not found with id: " + id));
        BeanUtils.copyProperties(shippingDTO, shipping, "id");
        Shipping updatedShipping = shippingRepository.save(shipping);
        ShippingDTO updatedDTO = new ShippingDTO();
        BeanUtils.copyProperties(updatedShipping, updatedDTO);
        return updatedDTO;
    }

    @Override
    public void deleteShipping(Long id) {
        if (!shippingRepository.existsById(id)) {
            throw new EntityNotFoundException("Shipping not found with id: " + id);
        }
        shippingRepository.deleteById(id);
    }

    @Override
    public ShippingDTO updateShippingStatus(Long id, String status) {
        Shipping shipping = shippingRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Shipping not found with id: " + id));
        shipping.setStatus(status);
        Shipping updatedShipping = shippingRepository.save(shipping);
        ShippingDTO updatedDTO = new ShippingDTO();
        BeanUtils.copyProperties(updatedShipping, updatedDTO);
        return updatedDTO;
    }
} 