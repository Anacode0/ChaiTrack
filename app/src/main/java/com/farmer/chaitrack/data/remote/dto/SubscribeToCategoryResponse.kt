package com.farmer.chaitrack.data.remote.dto

data class SubscribeToCategoryResponse(
    val success: Boolean,
    val message: String,
    val subscribedCategory: String
)