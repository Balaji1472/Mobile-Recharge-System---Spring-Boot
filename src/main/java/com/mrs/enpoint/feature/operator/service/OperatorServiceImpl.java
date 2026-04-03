package com.mrs.enpoint.feature.operator.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.mrs.enpoint.entity.Operator;
import com.mrs.enpoint.feature.auditlog.enums.AuditAction;
import com.mrs.enpoint.feature.auditlog.enums.EntityName;
import com.mrs.enpoint.feature.auditlog.service.AuditService;
import com.mrs.enpoint.feature.auth.enums.Status;
import com.mrs.enpoint.feature.operator.dto.OperatorRequestDTO;
import com.mrs.enpoint.feature.operator.dto.OperatorResponseDTO;
import com.mrs.enpoint.feature.operator.exception.DuplicateOperatorException;
import com.mrs.enpoint.feature.operator.exception.OperatorNotFoundException;
import com.mrs.enpoint.feature.operator.mapper.OperatorMapper;
import com.mrs.enpoint.feature.operator.repository.OperatorRepository;
import com.mrs.enpoint.feature.plan.repository.PlanRepository;
import com.mrs.enpoint.shared.exception.BusinessException;
import com.mrs.enpoint.shared.security.SecurityUtils;

@Service
public class OperatorServiceImpl implements OperatorService {

	private final PlanRepository planRepository;
	private final OperatorRepository operatorRepository;
	private final AuditService auditService;
	private final SecurityUtils securityUtils;

	public OperatorServiceImpl(OperatorRepository operatorRepository, AuditService auditService,
			SecurityUtils securityUtils, PlanRepository planRepository) {
		this.operatorRepository = operatorRepository;
		this.auditService = auditService;
		this.securityUtils = securityUtils;
		this.planRepository = planRepository;
	}

	@Override
	public List<OperatorResponseDTO> getAllOperators() {
		return operatorRepository.findAll().stream().map(OperatorMapper::toResponseDTO).collect(Collectors.toList());
	}

	@Override
	public OperatorResponseDTO getOperatorById(int id) {
		Operator operator = operatorRepository.findById(id)
				.orElseThrow(() -> new OperatorNotFoundException("Operator not found with id: " + id));
		return OperatorMapper.toResponseDTO(operator);
	}
 
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public OperatorResponseDTO createOperator(OperatorRequestDTO request) {
		String name = request.getOperatorName().toUpperCase().trim();

		if (operatorRepository.existsByOperatorName(name)) {
			throw new DuplicateOperatorException("Operator name already exists");
		}

		Operator operator = new Operator();
		operator.setName(name);
		operator.setStatus(Status.ACTIVE);

		Operator saved = operatorRepository.save(operator);

		auditService.log(securityUtils.getCurrentUserId(), EntityName.OPERATOR, saved.getOperatorId(),
				AuditAction.CREATE_OPERATOR, null, "Created: " + saved.getName());

		return OperatorMapper.toResponseDTO(saved);
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public OperatorResponseDTO updateOperator(int id, OperatorRequestDTO request) {
		Operator existing = operatorRepository.findById(id)
				.orElseThrow(() -> new OperatorNotFoundException("Operator not found with id: " + id));
		String oldName = existing.getName();
		existing.setName(request.getOperatorName());
		existing.setStatus(request.getStatus());
		Operator saved = operatorRepository.save(existing);

		auditService.log(securityUtils.getCurrentUserId(), EntityName.OPERATOR, saved.getOperatorId(),
				AuditAction.UPDATE_OPERATOR, oldName, saved.getName());

		return OperatorMapper.toResponseDTO(saved);
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public void activateOperator(int id) {
		Operator operator = operatorRepository.findById(id)
				.orElseThrow(() -> new OperatorNotFoundException("Operator not found with id: " + id));

		// Check if already active
		if (operator.getStatus() == Status.ACTIVE) {
			throw new BusinessException("Operator is already in active status.");
		}

		operator.setStatus(Status.ACTIVE);
		operatorRepository.save(operator);

		auditService.log(securityUtils.getCurrentUserId(), EntityName.OPERATOR, id, AuditAction.ACTIVATE_OPERATOR,
				Status.INACTIVE.toString(), Status.ACTIVE.toString());
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public void deactivateOperator(int id) {
		Operator operator = operatorRepository.findById(id)
				.orElseThrow(() -> new OperatorNotFoundException("Operator not found with id: " + id));

		// Check if already inactive
		if (operator.getStatus() == Status.INACTIVE) {
			throw new BusinessException("Operator is already in inactive status.");
		}

		// Validation: Check if there are active plans linked to this operator
		boolean hasActivePlans = planRepository.existsByOperator_OperatorIdAndIsActive(id, true);
		if (hasActivePlans) {
			throw new BusinessException("Cannot deactivate operator. There are active plans linked to this operator.");
		}

		operator.setStatus(Status.INACTIVE);
		operatorRepository.save(operator);

		auditService.log(securityUtils.getCurrentUserId(), EntityName.OPERATOR, id, AuditAction.DEACTIVATE_OPERATOR,
				Status.ACTIVE.toString(), Status.INACTIVE.toString());
	}

}