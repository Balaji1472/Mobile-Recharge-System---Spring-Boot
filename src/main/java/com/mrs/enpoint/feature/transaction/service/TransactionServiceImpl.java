package com.mrs.enpoint.feature.transaction.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mrs.enpoint.entity.Payment;
import com.mrs.enpoint.entity.RechargeTransaction;
import com.mrs.enpoint.entity.User;
import com.mrs.enpoint.feature.auth.repository.UserRepository;
import com.mrs.enpoint.feature.payment.repository.PaymentRepository;
import com.mrs.enpoint.feature.recharge.repository.RechargeTransactionRepository;
import com.mrs.enpoint.feature.transaction.dto.TransactionResponseDTO;
import com.mrs.enpoint.feature.transaction.mapper.TransactionMapper;
import com.mrs.enpoint.shared.exception.NotFoundException;
import com.mrs.enpoint.shared.security.SecurityUtils;

@Service
public class TransactionServiceImpl implements TransactionService {

	private final RechargeTransactionRepository rechargeRepository;
	private final PaymentRepository paymentRepository;
	private final UserRepository userRepository;
	private final SecurityUtils securityUtils;

	public TransactionServiceImpl(RechargeTransactionRepository rechargeRepository, PaymentRepository paymentRepository,
			UserRepository userRepository, SecurityUtils securityUtils) {
		this.rechargeRepository = rechargeRepository;
		this.paymentRepository = paymentRepository;
		this.userRepository = userRepository;
		this.securityUtils = securityUtils;
	}

	@Override
	@PreAuthorize("hasRole('USER')")
	public List<TransactionResponseDTO> getMyTransactions() {

		int currentUserId = securityUtils.getCurrentUserId();

		User user = userRepository.findById(currentUserId)
				.orElseThrow(() -> new NotFoundException("Logged-in user not found"));

		List<RechargeTransaction> recharges = rechargeRepository.findByUser_UserId(currentUserId);

		if (recharges.isEmpty()) {
			throw new NotFoundException("No transactions found for your accound");
		}

		List<TransactionResponseDTO> responseList = new ArrayList<>();

		for (RechargeTransaction r : recharges) {
			Payment payment = resolveLatestPayment(r.getRechargeId());
			TransactionResponseDTO dto = TransactionMapper.toResponseDTO(r, payment, user.getMobileNumber());
			responseList.add(dto);
		}

		return responseList;
	}

	@Override
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public TransactionResponseDTO getTransactionById(int rechargeId) {

		int currentUserId = securityUtils.getCurrentUserId();

		RechargeTransaction recharge = rechargeRepository.findById(rechargeId)
				.orElseThrow(() -> new NotFoundException("Transaction not found with recharge id: " + rechargeId));

		// user can only view their own transactions
		boolean isAdmin = isCurrentUserAdmin();
		if (isAdmin && recharge.getUser().getUserId() != currentUserId) {
			throw new AccessDeniedException("You are not authorized to view this transaction");
		}

		User user = userRepository.findById(recharge.getUser().getUserId())
				.orElseThrow(() -> new NotFoundException("User not found"));

		Payment payment = resolveLatestPayment(rechargeId);

		return TransactionMapper.toResponseDTO(recharge, payment, user.getMobileNumber());
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public List<TransactionResponseDTO> getAllTransactions() {

		List<RechargeTransaction> allRecharges = rechargeRepository.findAll();

		if (allRecharges.isEmpty()) {
			throw new NotFoundException("No transactions found in the system");
		}

		List<TransactionResponseDTO> responseList = new ArrayList<>();

		for (RechargeTransaction recharge : allRecharges) {

			User user = userRepository.findById(recharge.getUser().getUserId()).orElseThrow(
					() -> new NotFoundException("User not found for ID: " + recharge.getUser().getUserId()));

			Payment payment = resolveLatestPayment(recharge.getRechargeId());

			TransactionResponseDTO dto = TransactionMapper.toResponseDTO(recharge, payment, user.getMobileNumber());
			responseList.add(dto);
		}

		return responseList;
	}

	// fetch ltest payment attempt for a give recharge id
	private Payment resolveLatestPayment(int rechargeId) {
		return paymentRepository.findTopByRechargeTransaction_RechargeIdOrderByAttemptNumberDesc(rechargeId)
				.orElseThrow(() -> new NotFoundException("No payment record found for recharge id: " + rechargeId));
	}

	private boolean isCurrentUserAdmin() {
		return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
				.anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
	}

}
