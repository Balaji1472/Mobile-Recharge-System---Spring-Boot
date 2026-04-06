package com.mrs.enpoint.feature.notification.mapper;

import com.mrs.enpoint.entity.Notification;
import com.mrs.enpoint.feature.notification.dto.NotificationResponseDTO;

public class NotificationMapper {

    private NotificationMapper() {}

    public static NotificationResponseDTO toResponseDTO(Notification notification) {
        NotificationResponseDTO dto = new NotificationResponseDTO();
        dto.setNotificationId(notification.getNotificationId());
        dto.setUserId(notification.getUser().getUserId());
        dto.setType(notification.getType());
        dto.setMessage(notification.getMessage());
        dto.setSentAt(notification.getSentAt());
        dto.setReadStatus(notification.isReadStatus());
        return dto;
    }
}