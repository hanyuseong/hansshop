package com.example.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.shop.model.Category;
import com.example.shop.repository.CategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.shop.model.Category;
import com.example.shop.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }
    
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
    

    public List<Category> listCategories() {
        return categoryRepository.findAll();
    }
    
    public void updateCategory(Long id, Category category) {
        Category existingCategory = getCategoryById(id);
        existingCategory.setName(category.getName());
        categoryRepository.save(existingCategory);
    }
    
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid category ID: " + id));
    }



}