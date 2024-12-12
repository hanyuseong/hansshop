// Service Example (CartService.java)
package com.example.shop.service;

import com.example.shop.model.CartItem;

import com.example.shop.model.Product;
import com.example.shop.repository.ProductRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
@Slf4j
@Service
public class CartService {

    @Autowired
    private ProductRepository productRepository;

    private List<Product> cartItems = new ArrayList<>();

    public List<Product> getCartItems() {
        // Fetch all cart items from the repository
        return productRepository.findAll();
    }

    public void addToCart(Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product != null) {
            cartItems.add(product);
        }
    }

    public void removeFromCart(Long productId) {
        List<Product> cartItems = productRepository.findAll();
        Iterator<Product> iterator = cartItems.iterator();

        while (iterator.hasNext()) {
        	Product cartItem = iterator.next();
            if (cartItem.getId().equals(productId)) {
            	productRepository.delete(cartItem);
            }
        }
    }
}