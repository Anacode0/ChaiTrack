package com.farmer.chaitrack.data.remote.dto

data class NotificationApiResponse<T>(
    val success: Boolean,
    val data: T?,
    val message: String?
)

data class NotificationListResponse(
    val notifications: List<NotificationDto>
)