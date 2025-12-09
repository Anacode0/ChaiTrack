package com.farmer.chaitrack.data.remote.dto

data class SnoozeNotificationRequest(
    val notificationId: String,
    val snoozeDuration: String // e.g., "1h", "24h", "3d"
)