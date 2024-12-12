package com.example.shop.controller;

import com.example.shop.service.ProductService;
import com.example.shop.service.CartService;
import com.example.shop.service.CategoryService;
import com.example.shop.service.OrderService;
import com.example.shop.service.ExchangeService;
import com.example.shop.model.Product;
import com.example.shop.model.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ProductController {
    @Autowired
    private ProductService productService;
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ExchangeService exchangeService;

    @GetMapping("/")
    public String mainPage(Model model) {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> products = productService.getAllProducts(pageable);
        
        // 데이터가 비었을 경우 처리
        if (products.isEmpty()) {
            model.addAttribute("products", Page.empty());
        } else {
            model.addAttribute("products", products);
        }
        
        return "index";
    }
    
    @GetMapping("/admin")
    public String adminDashboard() {
        return "admin/index";
    }


    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "product/detail";
    }


    @GetMapping("/cart")
    public String showCart(Model model) {
        List<Product> cartItems = cartService.getCartItems();
        model.addAttribute("cartItems", cartItems != null ? cartItems : Collections.emptyList());
        return "cart";
    }

    @PostMapping("/cart/add/{id}")
    public String addToCart(@PathVariable("id") Long id) {
        cartService.addToCart(id);
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove/{id}")
    public String removeFromCart(@PathVariable("id") Long id) {
        cartService.removeFromCart(id);
        return "redirect:/cart";
    }

    @GetMapping("/orders")
    public String orderListPage(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "orders";
    }

    @PostMapping("/order")
    public String placeOrder() {
        orderService.placeOrder(cartService.getCartItems());
        return "redirect:/orders";
    }

    @PostMapping("/order/cancel/{id}")
    public String cancelOrder(@PathVariable("id") Long id) {
        orderService.cancelOrder(id);
        return "redirect:/orders";
    }

    @GetMapping("/exchanges")
    public String exchangeListPage(Model model) {
        model.addAttribute("exchanges", exchangeService.getAllExchanges());
        return "exchanges";
    }

    @PostMapping("/exchange/cancel/{id}")
    public String cancelExchange(@PathVariable("id") Long id) {
        exchangeService.cancelExchange(id);
        return "redirect:/exchanges";
    }

    @PostMapping("/return/cancel/{id}")
    public String cancelReturn(@PathVariable("id") Long id) {
        orderService.cancelReturn(id);
        return "redirect:/orders";
    }

    // Admin functionalities
    @GetMapping("/admin/products/list")
    public String listProducts(@RequestParam(defaultValue = "0") int page, Model model) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Product> productsPage = productService.getAllProducts(pageable);
        model.addAttribute("products", productsPage);
        return "admin/products";
    }

    @GetMapping("/admin/product/add")
    public String addProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.listCategories());
        return "admin/addProduct";
    }
   
    @PostMapping("/admin/product/add")
    public String addProduct(@ModelAttribute Product product, @RequestParam("image") MultipartFile image, RedirectAttributes redirectAttributes) {
        try {
            productService.saveProduct(product, image);
            redirectAttributes.addFlashAttribute("successMessage", "Product added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error adding product: " + e.getMessage());
        }
        return "redirect:/admin/products";
    }

    
    @GetMapping("/admin/product/edit/{id}")
    public String editProductForm(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "admin/editProduct";
    }
    
    @PostMapping("/admin/product/edit")
    public String editProduct(@ModelAttribute Product product, @RequestParam(value = "image", required = false) MultipartFile imageFile) {
        try {
            productService.saveProduct(product, imageFile);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the error accordingly, maybe redirect to an error page or show a message
        }
       
        return "redirect:/admin/products";
    }

    @PostMapping("/admin/product/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }
}