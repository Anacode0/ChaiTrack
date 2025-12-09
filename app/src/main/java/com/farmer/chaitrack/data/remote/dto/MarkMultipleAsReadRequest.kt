package com.farmer.chaitrack.data.remote.dto

data class MarkMultipleAsReadRequest(
    val notificationIds: List<String>
)