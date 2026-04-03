package com.mrs.enpoint.feature.category.service;

import java.util.List;

import com.mrs.enpoint.feature.category.dto.CategoryRequestDTO;
import com.mrs.enpoint.feature.category.dto.CategoryResponseDTO;

public interface CategoryService {

    CategoryResponseDTO createCategory(CategoryRequestDTO request);
    
    List<CategoryResponseDTO> getAllCategories();
    
    CategoryResponseDTO getCategoryById(int id);
    
    CategoryResponseDTO updateCategory(int id, CategoryRequestDTO request);
    
    void activateCategory(int id);
    
    void deactivateCategory(int id);
}