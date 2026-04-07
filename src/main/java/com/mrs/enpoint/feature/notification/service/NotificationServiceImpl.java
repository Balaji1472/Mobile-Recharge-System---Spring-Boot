package com.mrs.enpoint.feature.notification.service;

import com.mrs.enpoint.entity.Notification;
import com.mrs.enpoint.entity.User;
import com.mrs.enpoint.feature.auth.repository.UserRepository;
import com.mrs.enpoint.feature.notification.dto.NotificationResponseDTO;
import com.mrs.enpoint.feature.notification.enums.NotificationType;
import com.mrs.enpoint.feature.notification.mapper.NotificationMapper;
import com.mrs.enpoint.feature.notification.repository.NotificationRepository;
import com.mrs.enpoint.shared.exception.BusinessException;
import com.mrs.enpoint.shared.exception.NotFoundException;
import com.mrs.enpoint.shared.security.SecurityUtils;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

	private final NotificationRepository notificationRepository;
	private final UserRepository userRepository;
	private final SecurityUtils securityUtils;

	public NotificationServiceImpl(NotificationRepository notificationRepository, UserRepository userRepository,
			SecurityUtils securityUtils) {
		this.notificationRepository = notificationRepository;
		this.userRepository = userRepository;
		this.securityUtils = securityUtils;
	}

	@Override
	public void sendRechargeNotification(int userId, boolean success, String mobileNumber, String amount) {

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

		String message = success ? "Recharge successful for Rs." + amount + " on " + mobileNumber
				: "Recharge failed for Rs." + amount + " on " + mobileNumber + ". Please retry";

		Notification notification = new Notification();
		notification.setUser(user);
		notification.setType(NotificationType.RECHARGE);
		notification.setMessage(message);
		notification.setSentAt(LocalDateTime.now());
		notification.setReadStatus(false);

		notificationRepository.save(notification);
	}

	@Override
	public void sendRefundNotification(int userId, String mobileNumber, String amount) {

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BusinessException("User not found with id: " + userId));

		String message = "Refund of Rs." + amount + " has been processed for " + mobileNumber
				+ ". Amount will reflect in your account shortly.";

		Notification notification = new Notification();
		notification.setUser(user);
		notification.setType(NotificationType.RECHARGE);
		notification.setMessage(message);
		notification.setSentAt(LocalDateTime.now());
		notification.setReadStatus(false);
		
		notificationRepository.save(notification);
	}

	@Override
	@PreAuthorize("hasRole('USER')")
	public List<NotificationResponseDTO> getUnreadNotifications() {

		int currentUserId = securityUtils.getCurrentUserId();

		List<Notification> unread = notificationRepository.findByUser_UserIdAndReadStatusFalse(currentUserId);

		if (unread.isEmpty()) {
			throw new NotFoundException("No unread notifications found");
		}

		return unread.stream().map(notify -> NotificationMapper.toResponseDTO(notify)).collect(Collectors.toList());
	}

	@Override
	@PreAuthorize("hasRole('USER')")
	public List<NotificationResponseDTO> getReadNotifications() {

		int currentUserId = securityUtils.getCurrentUserId();

		List<Notification> read = notificationRepository.findByUser_UserIdAndReadStatusTrue(currentUserId);

		if (read.isEmpty()) {
			throw new NotFoundException("No read notifications found");
		}

		return read.stream().map(notify -> NotificationMapper.toResponseDTO(notify)).collect(Collectors.toList());
	}

	@Override
	@PreAuthorize("hasRole('USER')")
	@Transactional
	public NotificationResponseDTO markAsRead(int notificationId) {

		int currentUserId = securityUtils.getCurrentUserId();

		Notification notification = notificationRepository.findById(notificationId)
				.orElseThrow(() -> new NotFoundException("Notification not found with id: " + notificationId));

		// User can only mark their own notification
		if (notification.getUser().getUserId() != currentUserId) {
			throw new AccessDeniedException("You are not authorized to update this notification");
		}

		if (notification.isReadStatus()) {
			throw new BusinessException("Notification is already marked as read");
		}

		notification.setReadStatus(true);
		return NotificationMapper.toResponseDTO(notificationRepository.save(notification));
	}

	@Override
	@PreAuthorize("hasRole('USER')")
	@Transactional
	public void markAllAsRead() {

		int currentUserId = securityUtils.getCurrentUserId();

		List<Notification> unread = notificationRepository.findByUser_UserIdAndReadStatusFalse(currentUserId);

		if (unread.isEmpty()) {
			throw new NotFoundException("No unread notifications to mark as read");
		}

		unread.forEach(n -> n.setReadStatus(true));
		notificationRepository.saveAll(unread);
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public List<NotificationResponseDTO> getAllNotifications() {

		List<Notification> all = notificationRepository.findAll();

		if (all.isEmpty()) {
			throw new NotFoundException("No notifications found in the system");
		}

		return all.stream().map(notify -> NotificationMapper.toResponseDTO(notify)).collect(Collectors.toList());
	}
}