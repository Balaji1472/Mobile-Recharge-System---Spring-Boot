package com.mrs.enpoint.feature.category.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.mrs.enpoint.entity.Category;
import com.mrs.enpoint.feature.auditlog.enums.AuditAction;
import com.mrs.enpoint.feature.auditlog.enums.EntityName;
import com.mrs.enpoint.feature.auditlog.service.AuditService;
import com.mrs.enpoint.feature.category.dto.CategoryRequestDTO;
import com.mrs.enpoint.feature.category.dto.CategoryResponseDTO;
import com.mrs.enpoint.feature.category.mapper.CategoryMapper;
import com.mrs.enpoint.feature.category.repository.CategoryRepository;
import com.mrs.enpoint.feature.plan.repository.PlanRepository;
import com.mrs.enpoint.shared.exception.BusinessException;
import com.mrs.enpoint.shared.exception.DuplicateAlreadyExistsException;
import com.mrs.enpoint.shared.exception.NotFoundException;
import com.mrs.enpoint.shared.security.SecurityUtils;

import jakarta.transaction.Transactional;

@Service
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;
	private final PlanRepository planRepository;
	private final AuditService auditService;
	private final SecurityUtils securityUtils;

	public CategoryServiceImpl(CategoryRepository categoryRepository, PlanRepository planRepository,
			AuditService auditService, SecurityUtils securityUtils) {
		this.categoryRepository = categoryRepository;
		this.planRepository = planRepository;
		this.auditService = auditService;
		this.securityUtils = securityUtils;
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@Transactional
	public CategoryResponseDTO createCategory(CategoryRequestDTO request) {
		String code = request.getCategoryCode().toUpperCase().trim();

		if (categoryRepository.existsByCategoryCode(code)) {
			throw new DuplicateAlreadyExistsException("Category code already exists");
		}

		Category category = new Category();
		category.setCategoryCode(code);
		category.setDisplayName(request.getDisplayName());
		category.setIsActive(true);

		Category saved = categoryRepository.save(category);

		auditService.log(securityUtils.getCurrentUserId(), EntityName.CATEGORY, saved.getCategoryId(),
				AuditAction.CREATE_CATEGORY, null, "Created: " + saved.getCategoryCode());

		return CategoryMapper.toResponseDTO(saved);
	}

	@Override
	public List<CategoryResponseDTO> getAllCategories() {
	    // We use the new query that fetches categories and plans together
	    return categoryRepository.findAllWithPlanCount()
	            .stream()
	            .map(category -> CategoryMapper.toResponseDTO(category))
	            .collect(Collectors.toList());
	}

	@Override
	public CategoryResponseDTO getCategoryById(int id) {
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Category not found with id: " + id));
		return CategoryMapper.toResponseDTO(category);
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@Transactional
	public CategoryResponseDTO updateCategory(int id, CategoryRequestDTO request) {
		Category existing = categoryRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Category not found with id: " + id));
		String oldValue = existing.getDisplayName();
		existing.setDisplayName(request.getDisplayName());
		Category saved = categoryRepository.save(existing);

		auditService.log(securityUtils.getCurrentUserId(), EntityName.CATEGORY, saved.getCategoryId(),
				AuditAction.UPDATE_CATEGORY, oldValue, saved.getDisplayName());

		return CategoryMapper.toResponseDTO(saved);
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public void activateCategory(int id) {
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Category not found with id: " + id));
		category.setIsActive(true);
		categoryRepository.save(category);

		auditService.log(securityUtils.getCurrentUserId(), EntityName.CATEGORY, id, AuditAction.ACTIVATE_CATEGORY,
				"false", "true");
	} 

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public void deactivateCategory(int id) {
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Category not found with id: " + id));

		boolean hasActivePlans = planRepository.existsByCategory_CategoryIdAndIsActive(id, true);
		if (hasActivePlans) {
			throw new BusinessException("Cannot deactivate category. There are active plans linked to this category.");
		}

		category.setIsActive(false);
		categoryRepository.save(category);

		auditService.log(securityUtils.getCurrentUserId(), EntityName.CATEGORY, id, AuditAction.DEACTIVATE_CATEGORY,
				"true", "false");
	}

}