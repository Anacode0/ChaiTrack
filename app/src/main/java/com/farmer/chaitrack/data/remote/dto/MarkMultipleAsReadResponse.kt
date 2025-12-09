package com.farmer.chaitrack.data.remote.dto

data class MarkMultipleAsReadResponse(
    val success: Boolean,
    val message: String,
    val markedCount: Int,
    val failedIds: List<String> = emptyList()
)