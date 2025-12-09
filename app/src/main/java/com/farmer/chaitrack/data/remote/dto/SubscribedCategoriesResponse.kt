package com.farmer.chaitrack.data.remote.dto

data class SubscribedCategoriesResponse(
    val categories: List<NotificationCategory>,
    val totalSubscribed: Int
)

data class NotificationCategory(
    val id: String,
    val name: String,
    val description: String,
    val isSubscribed: Boolean,
    val icon: String? = null
)