package com.mrs.enpoint.feature.savednumber.service;

import com.mrs.enpoint.entity.MobileConnection;
import com.mrs.enpoint.entity.SavedMobileNumber;
import com.mrs.enpoint.entity.User;
import com.mrs.enpoint.feature.auth.repository.UserRepository;
import com.mrs.enpoint.feature.recharge.enums.ConnectionStatus;
import com.mrs.enpoint.feature.recharge.repository.MobileConnectionRepository;
import com.mrs.enpoint.feature.savednumber.dto.SavedNumberRequestDTO;
import com.mrs.enpoint.feature.savednumber.dto.SavedNumberResponseDTO;
import com.mrs.enpoint.feature.savednumber.dto.SavedNumberUpdateDTO;
import com.mrs.enpoint.feature.savednumber.mapper.SavedNumberMapper;
import com.mrs.enpoint.feature.savednumber.repository.SavedNumberRepository;
import com.mrs.enpoint.shared.exception.BusinessException;
import com.mrs.enpoint.shared.exception.DuplicateAlreadyExistsException;
import com.mrs.enpoint.shared.exception.NotFoundException;
import com.mrs.enpoint.shared.security.SecurityUtils;

import jakarta.transaction.Transactional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SavedNumberServiceImpl implements SavedNumberService {

	private final SavedNumberRepository savedNumberRepository;
	private final MobileConnectionRepository mobileConnectionRepository;
	private final UserRepository userRepository;
	private final SecurityUtils securityUtils;

	public SavedNumberServiceImpl(SavedNumberRepository savedNumberRepository,
			MobileConnectionRepository mobileConnectionRepository, UserRepository userRepository,
			SecurityUtils securityUtils) {
		this.savedNumberRepository = savedNumberRepository;
		this.mobileConnectionRepository = mobileConnectionRepository;
		this.userRepository = userRepository;
		this.securityUtils = securityUtils;
	}

	@Override
	@PreAuthorize("hasRole('USER')")
	public List<SavedNumberResponseDTO> getMySavedNumbers() {

		int currentUserId = securityUtils.getCurrentUserId();

		List<SavedMobileNumber> saved = savedNumberRepository.findByUser_UserId(currentUserId);

		if (saved.isEmpty()) {
			throw new NotFoundException("No saved numbers found for your account");
		}

		return saved.stream().map(save -> SavedNumberMapper.toResponseDTO(save)).collect(Collectors.toList());
	}

	@Override
	@PreAuthorize("hasRole('USER')")
	@Transactional
	public SavedNumberResponseDTO saveNumber(SavedNumberRequestDTO request) {

		int currentUserId = securityUtils.getCurrentUserId();

		// Validate mobile number exists in mobile_connection and is ACTIVE
		MobileConnection connection = mobileConnectionRepository.findByMobileNumber(request.getMobileNumber())
				.orElseThrow(() -> new NotFoundException(
						"Mobile number " + request.getMobileNumber() + " is not registered on this platform"));

		if (connection.getStatus() != ConnectionStatus.ACTIVE) {
			throw new BusinessException("Mobile number " + request.getMobileNumber() + " is not active");
		}

		// Prevent duplicate — same user cannot save same number twice
		if (savedNumberRepository.existsByUser_UserIdAndMobileNumber(currentUserId, request.getMobileNumber())) {
			throw new DuplicateAlreadyExistsException(
					"Mobile number " + request.getMobileNumber() + " is already saved in your list");
		}

		User user = userRepository.findById(currentUserId)
				.orElseThrow(() -> new NotFoundException("Logged-in user not found"));

		SavedMobileNumber savedNumber = new SavedMobileNumber();
		savedNumber.setUser(user);
		savedNumber.setMobileNumber(request.getMobileNumber());
		// operator_id auto-fetched from mobile_connection by the mobile number
		savedNumber.setOperator(connection.getOperator());
		savedNumber.setNickname(request.getNickname());

		return SavedNumberMapper.toResponseDTO(savedNumberRepository.save(savedNumber));
	}

	@Override
	@PreAuthorize("hasRole('USER')")
	@Transactional
	public SavedNumberResponseDTO updateNickname(int id, SavedNumberUpdateDTO request) {

		int currentUserId = securityUtils.getCurrentUserId();

		// ownership check: record must belong to the current user
		SavedMobileNumber savedNumber = savedNumberRepository.findByIdAndUser_UserId(id, currentUserId)
				.orElseThrow(() -> new NotFoundException("Saved number not found with id: " + id));

		savedNumber.setNickname(request.getNickname());

		return SavedNumberMapper.toResponseDTO(savedNumberRepository.save(savedNumber));
	}

	@Override
	@PreAuthorize("hasRole('USER')")
	public void deleteSavedNumber(int id) {

		int currentUserId = securityUtils.getCurrentUserId();

		// ownership check: record must belong to the current user
		SavedMobileNumber savedNumber = savedNumberRepository.findByIdAndUser_UserId(id, currentUserId)
				.orElseThrow(() -> new NotFoundException("Saved number not found with id: " + id));

		savedNumberRepository.delete(savedNumber);
	}
}