package com.farmer.chaitrack.data.remote.dto


data class NotificationData(
    val notifications: List<NotificationDto>
)

data class NotificationDto(
    val id: String,
    val type: String,
    val title: String,
    val message: String,
    val timestamp: String,
    val isRead: Boolean? = false,
    val readAt: String? = null,
    val priority: String? = "NORMAL",
    val actionType: String? = null,
    val actionData: String? = null,
    val iconUrl: String? = null,
    val imageUrl: String? = null,
    val category: String? = null,
    val expiresAt: String? = null,
    val soundEnabled: Boolean? = true,
    val vibrationEnabled: Boolean? = true,
    val groupKey: String? = null,
    val createdAt: String,
    val updatedAt: String
)

data class NotificationStatisticsResponse(
    val success: Boolean,
    val message: String? = null,
    val data: NotificationStatisticsData
)

data class NotificationStatisticsData(
    val totalNotifications: Int,
    val unreadCount: Int,
    val readCount: Int,
    val byType: Map<String, Int>,
    val byPriority: Map<String, Int>
)

// Generic response wrapper
data class BaseResponse<T>(
    val success: Boolean,
    val message: String? = null,
    val data: T? = null
)

// For simple boolean responses
data class SimpleResponse(
    val success: Boolean,
    val message: String? = null
)