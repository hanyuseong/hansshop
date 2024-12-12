// Service Example (OrderService.java)
package com.example.shop.service;

import com.example.shop.model.Order;
import com.example.shop.model.Product;
import com.example.shop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    private List<Order> orders = new ArrayList<>();

    public List<Order> getAllOrders() {
        return orders;
    }

    public void placeOrder(List<Product> products) {
        Order order = new Order(products);
        orders.add(order);
        orderRepository.save(order);
    }

    public void cancelOrder(Long orderId) {
        orders.removeIf(order -> order.getId().equals(orderId));
        orderRepository.deleteById(orderId);
    }

    public void cancelReturn(Long orderId) {
        Order order = orders.stream().filter(o -> o.getId().equals(orderId)).findFirst().orElse(null);
        if (order != null) {
            order.setStatus("Return Cancelled");
        }
    }
}