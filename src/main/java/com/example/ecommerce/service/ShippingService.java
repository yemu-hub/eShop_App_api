package com.example.ecommerce.service;

import com.example.ecommerce.dto.ShippingDTO;
import java.util.List;

public interface ShippingService {
    ShippingDTO createShipping(ShippingDTO shippingDTO);
    ShippingDTO getShippingById(Long id);
    List<ShippingDTO> getAllShippings();
    List<ShippingDTO> getShippingsByStatus(String status);
    List<ShippingDTO> getShippingsByMethod(String method);
    List<ShippingDTO> getShippingsByOrder(Long orderId);
    List<ShippingDTO> getShippingsByTrackingNumber(String trackingNumber);
    List<ShippingDTO> getShippingsByCountry(String country);
    List<ShippingDTO> getShippingsByState(String state);
    List<ShippingDTO> getShippingsByStatusAndMethod(String status, String method);
    ShippingDTO updateShipping(Long id, ShippingDTO shippingDTO);
    void deleteShipping(Long id);
    ShippingDTO updateShippingStatus(Long id, String status);
} 