package com.farmer.chaitrack.data.remote.dto

data class MarkAllAsReadResponse(
    val success: Boolean,
    val message: String,
    val markedCount: Int
)