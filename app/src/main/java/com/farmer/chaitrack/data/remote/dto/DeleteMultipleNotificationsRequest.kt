package com.farmer.chaitrack.data.remote.dto

data class DeleteMultipleNotificationsRequest(
    val notificationIds: List<String>
)