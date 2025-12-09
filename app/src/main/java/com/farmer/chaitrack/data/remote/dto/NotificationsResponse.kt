package com.farmer.chaitrack.data.remote.dto

data class NotificationsResponse(
    val notifications: List<Notification>,
    val currentPage: Int,
    val totalPages: Int,
    val totalNotifications: Int,
    val hasNext: Boolean,
    val hasPrevious: Boolean,
    val success: Boolean,
    val message: String? = null,
    val data: NotificationData
)