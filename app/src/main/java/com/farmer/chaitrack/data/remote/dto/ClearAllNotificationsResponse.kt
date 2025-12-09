package com.farmer.chaitrack.data.remote.dto

data class ClearAllNotificationsResponse(
    val success: Boolean,
    val message: String,
    val clearedCount: Int
)