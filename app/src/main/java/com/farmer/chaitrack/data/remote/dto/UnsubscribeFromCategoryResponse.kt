package com.farmer.chaitrack.data.remote.dto

data class UnsubscribeFromCategoryResponse(
    val success: Boolean,
    val message: String,
    val unsubscribedCategory: String
)