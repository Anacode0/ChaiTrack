package com.farmer.chaitrack.data.remote.dto

data class SnoozeNotificationResponse(
    val success: Boolean,
    val message: String,
    val snoozedUntil: String
)