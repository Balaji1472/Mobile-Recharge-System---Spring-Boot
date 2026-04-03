package com.mrs.enpoint.feature.category.controller;

import com.mrs.enpoint.feature.category.dto.CategoryRequestDTO;
import com.mrs.enpoint.feature.category.dto.CategoryResponseDTO;
import com.mrs.enpoint.feature.category.service.CategoryService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("http://localhost:5173/")
@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    } 

    //create
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(@RequestBody CategoryRequestDTO request) {
        return new ResponseEntity<>(categoryService.createCategory(request), HttpStatus.CREATED);
    }

    //get all
    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    //get by id
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable int id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable int id, @RequestBody CategoryRequestDTO request) {
        return ResponseEntity.ok(categoryService.updateCategory(id, request));
    }

    //activate
    @PutMapping("/{id}/activate")
    public ResponseEntity<String> activateCategory(@PathVariable int id) {
        categoryService.activateCategory(id);
        return ResponseEntity.ok("Category activated successfully");
    }

    //deactivate
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<String> deactivateCategory(@PathVariable int id) {
        categoryService.deactivateCategory(id);
        return ResponseEntity.ok("Category deactivated successfully");
    }
}