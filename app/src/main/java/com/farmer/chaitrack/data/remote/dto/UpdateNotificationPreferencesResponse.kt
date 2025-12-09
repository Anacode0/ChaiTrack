package com.farmer.chaitrack.data.remote.dto

data class UpdateNotificationPreferencesResponse(
    val success: Boolean,
    val message: String,
    val updatedPreferences: NotificationPreferences
)