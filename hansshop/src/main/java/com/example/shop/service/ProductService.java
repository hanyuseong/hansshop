package com.example.shop.service;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.shop.model.Product;
import com.example.shop.repository.ProductRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Page<Product> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Product getProductById(Long id) {
                return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    @Transactional
    public Product saveProduct(Product product, MultipartFile imageFile) throws IOException {
        if (product.getId() == null) {
            product.setCreatedAt(LocalDateTime.now());
        }
        product.setUpdatedAt(LocalDateTime.now());
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = saveImage(imageFile);
            product.setImageUrl(imageUrl);
        }
       return productRepository.save(product);
    }
    
    @Transactional
    public void deleteProduct(Long id) {
        Iterable<Product> products = productRepository.findAll(); // 모든 제품 가져오기
        for (Product product : products) {
            if (product.getId().equals(id)) { // 주어진 ID와 일치하는 경우
            	log.debug("id : " + id + "product : " + product);
                productRepository.delete(product); // 삭제
                break; // 루프 종료
            }
        }
    }
    
    private String saveImage(MultipartFile imageFile) throws IOException {
        String folder = "D:/03.project/hansshop/src/main/resources/static/uploads/";
        String webPath = "/uploads/";
        File uploadDir = new File(folder);
        log.debug("uploadDir = {}", uploadDir);
        if (!uploadDir.exists()) {
            log.debug("dir 생성");
            uploadDir.mkdirs();
        }
      
        byte[] bytes = imageFile.getBytes();
        Path path = Paths.get(folder + imageFile.getOriginalFilename());
        log.debug("파일업로드");
        Files.write(path, bytes);
        return webPath + imageFile.getOriginalFilename();
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        log.debug("ddddddddddd"+pageable);
        return productRepository.findAll(pageable);

    }

}
