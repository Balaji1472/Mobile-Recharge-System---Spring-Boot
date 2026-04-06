package com.mrs.enpoint.feature.notification.controller;

import com.mrs.enpoint.feature.notification.dto.NotificationResponseDTO;
import com.mrs.enpoint.feature.notification.service.NotificationService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/unread")
    public ResponseEntity<List<NotificationResponseDTO>> getUnreadNotifications() {
        return ResponseEntity.ok(notificationService.getUnreadNotifications());
    }

    @GetMapping("/readed")
    public ResponseEntity<List<NotificationResponseDTO>> getReadNotifications() {
        return ResponseEntity.ok(notificationService.getReadNotifications());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<NotificationResponseDTO> markAsRead(@PathVariable int id) {
        return ResponseEntity.ok(notificationService.markAsRead(id));
    }

    @PatchMapping("/read-all")
    public ResponseEntity<String> markAllAsRead() {
        notificationService.markAllAsRead();
        return ResponseEntity.ok("All notifications marked as read");
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponseDTO>> getAllNotifications() {
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }
}