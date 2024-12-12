package com.example.shop.controller;

import com.example.shop.service.ProductService;
import com.example.shop.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ProductService productService;

    @GetMapping("/products")
    public String productList(Model model, @PageableDefault(size = 10) Pageable pageable) {
        model.addAttribute("products", productService.getProducts(pageable));
        return "admin/products";
    }

    @GetMapping("/products/new")
    public String productForm(Model model) {
        model.addAttribute("product", new Product());
        return "admin/product-form";
    }

    @PostMapping("/products/new")
    public String createProduct(Product product, @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        productService.saveProduct(product, imageFile);
        return "redirect:/admin/products";
    }
}