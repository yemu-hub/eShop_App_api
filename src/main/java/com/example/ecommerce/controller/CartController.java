package com.example.ecommerce.controller;

import com.example.ecommerce.model.Cart;
import com.example.ecommerce.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getActiveCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getActiveCart(userId));
    }

    @PostMapping("/{userId}/items")
    public ResponseEntity<Cart> addItemToCart(
        @PathVariable Long userId,
        @RequestParam Long productId,
        @RequestParam int quantity
    ) {
        return ResponseEntity.ok(cartService.addItemToCart(userId, productId, quantity));
    }

    @PutMapping("/{userId}/items/{cartItemId}")
    public ResponseEntity<Cart> updateCartItem(
        @PathVariable Long userId,
        @PathVariable Long cartItemId,
        @RequestParam int quantity
    ) {
        return ResponseEntity.ok(cartService.updateCartItem(userId, cartItemId, quantity));
    }

    @DeleteMapping("/{userId}/items/{cartItemId}")
    public ResponseEntity<Cart> removeCartItem(
        @PathVariable Long userId,
        @PathVariable Long cartItemId
    ) {
        return ResponseEntity.ok(cartService.removeCartItem(userId, cartItemId));
    }
}