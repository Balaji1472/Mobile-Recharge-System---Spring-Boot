package com.mrs.enpoint.feature.analytics.service;

import com.mrs.enpoint.feature.analytics.dto.AdminAnalyticsDTO;
import com.mrs.enpoint.feature.analytics.dto.UserAnalyticsDTO;

public interface AnalyticsService {

    AdminAnalyticsDTO getAdminOverview();

    UserAnalyticsDTO getUserOverview();
}