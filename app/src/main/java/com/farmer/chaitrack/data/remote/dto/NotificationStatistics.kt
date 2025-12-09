package com.farmer.chaitrack.data.remote.dto

import com.farmer.chaitrack.data.remote.dto.*
data class NotificationStatistics(
    val totalNotifications: Int,
    val unreadCount: Int,
    val success: Boolean,
    val readCount: Int,
    val deliveryNotifications: Int,
    val fertilizerNotifications: Int,
    val financeNotifications: Int,
    val announcementNotifications: Int,
    val todayCount: Int,
    val weekCount: Int,
    val monthCount: Int,
    val data: NotificationStatisticsData?,
    val message: String?
)