package com.mrs.enpoint.feature.analytics.controller;

import com.mrs.enpoint.feature.analytics.dto.AdminAnalyticsDTO;
import com.mrs.enpoint.feature.analytics.dto.UserAnalyticsDTO;
import com.mrs.enpoint.feature.analytics.service.AnalyticsService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping
    public ResponseEntity<Object> getOverview(Authentication authentication) {
  
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            AdminAnalyticsDTO adminDto = analyticsService.getAdminOverview();
            return ResponseEntity.ok(adminDto);
        } else {
            UserAnalyticsDTO userDto = analyticsService.getUserOverview();
            return ResponseEntity.ok(userDto);
        }
    }
}