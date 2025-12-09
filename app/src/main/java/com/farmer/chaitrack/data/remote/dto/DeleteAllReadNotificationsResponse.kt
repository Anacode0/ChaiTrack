package com.farmer.chaitrack.data.remote.dto

data class DeleteAllReadNotificationsResponse(
    val success: Boolean,
    val message: String,
    val deletedCount: Int
)