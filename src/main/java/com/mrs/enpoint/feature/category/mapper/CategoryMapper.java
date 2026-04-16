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
		if (category.getPlans() != null) {
            dto.setTotalPlans(category.getPlans().size());
        } else {
            dto.setTotalPlans(0);
        }
		dto.setIsActive(category.getIsActive());
		return dto;
	}
}