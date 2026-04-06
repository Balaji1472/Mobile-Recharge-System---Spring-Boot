package com.mrs.enpoint.feature.notification.repository;

import com.mrs.enpoint.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    // All notifications for a user
    List<Notification> findByUser_UserId(int userId);

    // Unread notifications for a user
    List<Notification> findByUser_UserIdAndReadStatusFalse(int userId);

    // Read notifications for a user
    List<Notification> findByUser_UserIdAndReadStatusTrue(int userId);
}