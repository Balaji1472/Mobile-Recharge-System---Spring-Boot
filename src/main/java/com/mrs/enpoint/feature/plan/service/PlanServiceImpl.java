package com.mrs.enpoint.feature.plan.service;

import com.mrs.enpoint.entity.Category;
import com.mrs.enpoint.entity.Operator;
import com.mrs.enpoint.entity.Plan;
import com.mrs.enpoint.feature.auditlog.enums.AuditAction;
import com.mrs.enpoint.feature.auditlog.enums.EntityName;
import com.mrs.enpoint.feature.auditlog.service.AuditService;
import com.mrs.enpoint.feature.category.repository.CategoryRepository;
import com.mrs.enpoint.feature.operator.repository.OperatorRepository;
import com.mrs.enpoint.feature.plan.dto.PlanRequestDTO;
import com.mrs.enpoint.feature.plan.dto.PlanResponseDTO;
import com.mrs.enpoint.feature.plan.mapper.PlanMapper;
import com.mrs.enpoint.feature.plan.repository.PlanRepository;
import com.mrs.enpoint.shared.exception.BusinessException;
import com.mrs.enpoint.shared.exception.DuplicateAlreadyExistsException;
import com.mrs.enpoint.shared.exception.NotFoundException;
import com.mrs.enpoint.shared.security.SecurityUtils;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlanServiceImpl implements PlanService {

	private final PlanRepository planRepository;
	private final OperatorRepository operatorRepository;
	private final CategoryRepository categoryRepository;
	private final AuditService auditService;
	private final SecurityUtils securityUtils;

	public PlanServiceImpl(PlanRepository planRepository, OperatorRepository operatorRepository,
			CategoryRepository categoryRepository, AuditService auditService, SecurityUtils securityUtils) {
		this.planRepository = planRepository;
		this.operatorRepository = operatorRepository;
		this.categoryRepository = categoryRepository;
		this.auditService = auditService;
		this.securityUtils = securityUtils;
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public PlanResponseDTO createPlan(PlanRequestDTO request) {
		validatePlanRequest(request);

		Operator operator = operatorRepository.findById(request.getOperatorId())
				.orElseThrow(() -> new NotFoundException("Operator not found with id: " + request.getOperatorId()));

		Category category = categoryRepository.findById(request.getCategoryId())
				.orElseThrow(() -> new NotFoundException("Category not found with id: " + request.getCategoryId()));

		String planName = request.getPlanName().trim();

		if (planRepository.existsByOperator_OperatorIdAndPlanName(request.getOperatorId(), planName)) {
			throw new DuplicateAlreadyExistsException(
					"Plan with name '" + planName + "' already exists under this operator");
		}

		Plan plan = new Plan();
		plan.setOperator(operator);
		plan.setCategory(category);
		plan.setPlanName(planName);
		plan.setPrice(request.getPrice());
		plan.setValidityDays(request.getValidityDays());
		plan.setDataBenefits(request.getDataBenefits());
		plan.setCallBenefits(request.getCallBenefits());
		plan.setSmsBenefits(request.getSmsBenefits());
		plan.setIsActive(true);

		Plan saved = planRepository.save(plan);

		auditService.log(securityUtils.getCurrentUserId(), EntityName.PLAN, saved.getPlanId(), AuditAction.CREATE_PLAN,
				null, "Created: " + saved.getPlanName() + ", Price: " + saved.getPrice());

		return PlanMapper.toResponseDTO(saved);
	}

	@Override
	public List<PlanResponseDTO> getAllPlans() {
		return planRepository.findAll().stream().map(plan -> PlanMapper.toResponseDTO(plan))
				.collect(Collectors.toList());
	}

	@Override
	public PlanResponseDTO getPlanById(int id) {
		Plan plan = planRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Plan not found with id: " + id));
		return PlanMapper.toResponseDTO(plan);
	}

	@Override
	public List<PlanResponseDTO> getPlansByOperator(int operatorId) {
		if (!operatorRepository.existsById(operatorId)) {
			throw new NotFoundException("Operator not found with id: " + operatorId);
		}
		return planRepository.findByOperator_OperatorId(operatorId).stream().map(plan -> PlanMapper.toResponseDTO(plan))
				.collect(Collectors.toList());
	}

	@Override
	public List<PlanResponseDTO> getPlansByCategory(int categoryId) {
		if (!categoryRepository.existsById(categoryId)) {
			throw new NotFoundException("Category not found with id: " + categoryId);
		}
		return planRepository.findByCategory_CategoryId(categoryId).stream().map(plan -> PlanMapper.toResponseDTO(plan))
				.collect(Collectors.toList());
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public PlanResponseDTO updatePlan(int id, PlanRequestDTO request) {
		validatePlanRequest(request);

		Plan existing = planRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Plan not found with id: " + id));

		// If plan name changed, check for duplicate under the same operator
		String planName = request.getPlanName().trim();
		if (!existing.getPlanName().equals(planName) && planRepository
				.existsByOperator_OperatorIdAndPlanName(existing.getOperator().getOperatorId(), planName)) {
			throw new DuplicateAlreadyExistsException(
					"Plan with name '" + planName + "' already exists under this operator");
		}

		Category category = categoryRepository.findById(request.getCategoryId())
				.orElseThrow(() -> new NotFoundException("Category not found with id: " + request.getCategoryId()));

		String oldValue = "name=" + existing.getPlanName() + ", price=" + existing.getPrice();

		existing.setCategory(category);
		existing.setPlanName(planName);
		existing.setPrice(request.getPrice());
		existing.setValidityDays(request.getValidityDays());
		existing.setDataBenefits(request.getDataBenefits());
		existing.setCallBenefits(request.getCallBenefits());
		existing.setSmsBenefits(request.getSmsBenefits());

		Plan saved = planRepository.save(existing);

		// Check if price changed — log UPDATE_PRICE separately
		if (!oldValue.contains("price=" + saved.getPrice())) {
			auditService.log(securityUtils.getCurrentUserId(), EntityName.PLAN, saved.getPlanId(),
					AuditAction.UPDATE_PRICE, oldValue, "price=" + saved.getPrice());
		}

		auditService.log(securityUtils.getCurrentUserId(), EntityName.PLAN, saved.getPlanId(),
				AuditAction.UPDATE_PLAN_NAME, oldValue, "name=" + saved.getPlanName());

		return PlanMapper.toResponseDTO(saved);
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public void activatePlan(int id) {
		Plan plan = planRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Plan not found with id: " + id));
		plan.setIsActive(true);
		planRepository.save(plan);

		auditService.log(securityUtils.getCurrentUserId(), EntityName.PLAN, id, AuditAction.ACTIVATE_PLAN, "false",
				"true");
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public void deactivatePlan(int id) {
		Plan plan = planRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Plan not found with id: " + id));
		plan.setIsActive(false);
		planRepository.save(plan);

		auditService.log(securityUtils.getCurrentUserId(), EntityName.PLAN, id, AuditAction.DEACTIVATE_PLAN, "true",
				"false");
	}

	// helper method
	private void validatePlanRequest(PlanRequestDTO request) {
		if (request.getPlanName() == null || request.getPlanName().trim().isEmpty()) {
			throw new BusinessException("Plan name must not be blank");
		}
		if (request.getPrice() == null) {
			throw new BusinessException("Price must not be null");
		}
		if (request.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
			throw new BusinessException("Price must be greater than zero");
		}
		if (request.getOperatorId() <= 0) {
			throw new BusinessException("Valid operator id is required");
		}
		if (request.getCategoryId() <= 0) {
			throw new BusinessException("Valid category id is required");
		}
	}
}