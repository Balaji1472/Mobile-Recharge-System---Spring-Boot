package com.mrs.enpoint.feature.invoice.service;

import com.mrs.enpoint.entity.Payment;
import com.mrs.enpoint.entity.RechargeInvoice;
import com.mrs.enpoint.entity.RechargeTransaction;
import com.mrs.enpoint.feature.invoice.dto.InvoiceResponseDTO;
import com.mrs.enpoint.feature.invoice.mapper.InvoiceMapper;
import com.mrs.enpoint.feature.invoice.repository.InvoiceRepository;
import com.mrs.enpoint.feature.payment.repository.PaymentRepository;
import com.mrs.enpoint.feature.recharge.repository.RechargeTransactionRepository;
import com.mrs.enpoint.shared.exception.NotFoundException;
import com.mrs.enpoint.shared.security.SecurityUtils;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {

	private final InvoiceRepository invoiceRepository;
	private final RechargeTransactionRepository rechargeRepository;
	private final PaymentRepository paymentRepository;
	private final SecurityUtils securityUtils;

	public InvoiceServiceImpl(InvoiceRepository invoiceRepository, RechargeTransactionRepository rechargeRepository,
			PaymentRepository paymentRepository, SecurityUtils securityUtils) {
		this.invoiceRepository = invoiceRepository;
		this.rechargeRepository = rechargeRepository;
		this.paymentRepository = paymentRepository;
		this.securityUtils = securityUtils;
	}

	@Override
	public void generateInvoice(int rechargeId) {

		RechargeTransaction recharge = rechargeRepository.findById(rechargeId)
				.orElseThrow(() -> new NotFoundException("Recharge not found with id: " + rechargeId));


		RechargeInvoice invoice = new RechargeInvoice();
		invoice.setRechargeTransaction(recharge);
		invoice.setGeneratedAt(LocalDateTime.now());

		invoiceRepository.save(invoice);
	}

	@Override
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public InvoiceResponseDTO getInvoiceById(int invoiceId) {

		int currentUserId = securityUtils.getCurrentUserId();

		RechargeInvoice invoice = invoiceRepository.findById(invoiceId)
				.orElseThrow(() -> new NotFoundException("Invoice not found with id: " + invoiceId));

		// USER can only view their own invoices
		boolean isAdmin = isCurrentUserAdmin();
		if (!isAdmin && invoice.getRechargeTransaction().getUser().getUserId() != currentUserId) {
			throw new AccessDeniedException("You are not authorized to view this invoice");
		}

		Payment payment = resolveLatestPayment(invoice.getRechargeTransaction().getRechargeId());

		return InvoiceMapper.toResponseDTO(invoice, payment);
	}

	@Override
	@PreAuthorize("hasRole('USER')")
	public List<InvoiceResponseDTO> getMyInvoices() {

		int currentUserId = securityUtils.getCurrentUserId();

		List<RechargeInvoice> invoices = invoiceRepository.findByRechargeTransaction_User_UserId(currentUserId);

		if (invoices.isEmpty()) {
			throw new NotFoundException("No invoices found for your account");
		}

		return invoices.stream().map(invoice -> {
			Payment payment = resolveLatestPayment(invoice.getRechargeTransaction().getRechargeId());
			return InvoiceMapper.toResponseDTO(invoice, payment);
		}).collect(Collectors.toList());
	}

	@Override
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public InvoiceResponseDTO getInvoiceByRechargeId(int rechargeId) {

		int currentUserId = securityUtils.getCurrentUserId();

		RechargeInvoice invoice = invoiceRepository.findByRechargeTransaction_RechargeId(rechargeId)
				.orElseThrow(() -> new NotFoundException("Invoice not found for recharge id: " + rechargeId));

		// USER can only view their own invoices
		boolean isAdmin = isCurrentUserAdmin();
		if (!isAdmin && invoice.getRechargeTransaction().getUser().getUserId() != currentUserId) {
			throw new AccessDeniedException("You are not authorized to view this invoice");
		}

		Payment payment = resolveLatestPayment(rechargeId);

		return InvoiceMapper.toResponseDTO(invoice, payment);
	}

	// ─── Helpers ─────────────────────────────────────────────────────────────

	private Payment resolveLatestPayment(int rechargeId) {
		return paymentRepository.findTopByRechargeTransaction_RechargeIdOrderByAttemptNumberDesc(rechargeId)
				.orElseThrow(() -> new NotFoundException("No payment record found for recharge id: " + rechargeId));
	}

	private boolean isCurrentUserAdmin() {
		return SecurityContextHolder.getContext().getAuthentication()
				.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
	}
}