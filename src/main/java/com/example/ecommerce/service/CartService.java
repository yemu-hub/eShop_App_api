package com.example.ecommerce.service;

import com.example.ecommerce.model.Cart;

public interface CartService {
    Cart getActiveCart(Long userId);
    Cart addItemToCart(Long userId, Long productId, int quantity);
    Cart updateCartItem(Long userId, Long cartItemId, int quantity);
    Cart removeCartItem(Long userId, Long cartItemId);
}