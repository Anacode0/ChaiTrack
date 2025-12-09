package com.farmer.chaitrack.data.remote.dto

data class TestNotificationResponse(
    val success: Boolean,
    val message: String,
    val testNotificationId: String,
    val deliveredAt: String? // ISO format
)