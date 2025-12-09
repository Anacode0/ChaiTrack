package com.farmer.chaitrack.data.remote.dto

data class DeleteMultipleNotificationsResponse(
    val success: Boolean,
    val message: String,
    val deletedCount: Int,
    val failedIds: List<String> = emptyList()
)