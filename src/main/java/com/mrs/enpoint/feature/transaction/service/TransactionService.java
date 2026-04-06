package com.mrs.enpoint.feature.transaction.service;

import java.util.List;

import com.mrs.enpoint.feature.transaction.dto.TransactionResponseDTO;

public interface TransactionService {

	
	List<TransactionResponseDTO> getMyTransactions();
	
	TransactionResponseDTO getTransactionById(int rechargeId);
	
	List<TransactionResponseDTO> getAllTransactions();
}
