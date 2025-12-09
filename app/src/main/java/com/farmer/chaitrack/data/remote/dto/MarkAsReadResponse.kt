package com.farmer.chaitrack.data.remote.dto

data class MarkAsReadResponse(
    val success: Boolean,
    val message: String,
    val markedNotificationId: String
)