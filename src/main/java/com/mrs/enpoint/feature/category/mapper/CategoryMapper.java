package com.mrs.enpoint.feature.category.mapper;

import com.mrs.enpoint.entity.Category;
import com.mrs.enpoint.feature.category.dto.CategoryResponseDTO;

public class CategoryMapper {

	private CategoryMapper() {
		
	}
	
	public static CategoryResponseDTO toResponseDTO(Category category) {
		CategoryResponseDTO dto = new CategoryResponseDTO();
		dto.setCategoryId(category.getCategoryId());
		dto.setCategoryCode(category.getCategoryCode());
		dto.setDisplayName(category.getDisplayName());
		dto.setIsActive(category.getIsActive());
		return dto;
	}
}