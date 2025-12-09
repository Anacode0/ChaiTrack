package com.farmer.chaitrack.data.remote.dto

data class DeleteNotificationResponse(
    val success: Boolean,
    val message: String,
    val deletedNotificationId: String
)