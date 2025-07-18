package com.example.ecommerce.service.impl;

import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.Customer;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.CustomerRepository;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional(readOnly = true)
    public Cart getActiveCart(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
        
        return cartRepository.findByCustomerAndActiveTrue(customer)
            .orElseGet(() -> createNewCart(customer));
    }

    @Override
    @Transactional
    public Cart addItemToCart(Long customerId, Long productId, int quantity) {
        Cart cart = getActiveCart(customerId);
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        
        CartItem cartItem = cart.getCartItems().stream()
            .filter(item -> item.getProduct().getId().equals(productId))
            .findFirst()
            .orElseGet(() -> {
                CartItem newItem = new CartItem();
                newItem.setCart(cart);
                newItem.setProduct(product);
                cart.getCartItems().add(newItem);
                return newItem;
            });
        
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cartItem.setUnitPrice(product.getPrice());
        cartItem.calculateSubtotal();
        cart.calculateTotals();
        
        return cartRepository.save(cart);
    }

    @Override
    @Transactional
    public Cart updateCartItem(Long customerId, Long cartItemId, int quantity) {
        Cart cart = getActiveCart(customerId);
        CartItem cartItem = cart.getCartItems().stream()
            .filter(item -> item.getId().equals(cartItemId))
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItemId));
        
        cartItem.setQuantity(quantity);
        cartItem.calculateSubtotal();
        cart.calculateTotals();
        
        return cartRepository.save(cart);
    }

    @Override
    @Transactional
    public Cart removeCartItem(Long customerId, Long cartItemId) {
        Cart cart = getActiveCart(customerId);
        cart.getCartItems().removeIf(item -> item.getId().equals(cartItemId));
        cart.calculateTotals();
        
        return cartRepository.save(cart);
    }

    private Cart createNewCart(Customer customer) {
        Cart cart = new Cart();
        cart.setCustomer(customer);
        cart.setActive(true);
        return cartRepository.save(cart);
    }
}