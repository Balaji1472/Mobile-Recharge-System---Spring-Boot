package com.mrs.enpoint.feature.notification.service;

import com.mrs.enpoint.feature.notification.dto.NotificationResponseDTO;

import java.util.List;

public interface NotificationService {

    void sendRechargeNotification(int userId, boolean success, String mobileNumber, String amount);

    List<NotificationResponseDTO> getUnreadNotifications();

    List<NotificationResponseDTO> getReadNotifications();

    NotificationResponseDTO markAsRead(int notificationId);

    void markAllAsRead();

    List<NotificationResponseDTO> getAllNotifications();
}