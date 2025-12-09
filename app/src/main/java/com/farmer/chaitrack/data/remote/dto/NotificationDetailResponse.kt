package com.farmer.chaitrack.data.remote.dto

data class NotificationDetailResponse(
    val notification: Notification,
    val relatedData: Map<String, Any>? = null,
    val actions: List<NotificationAction>? = null,
    val metadata: NotificationMetadata? = null
)

data class NotificationAction(
    val label: String,
    val type: String, // "deep_link", "url", "api_call", "dismiss"
    val action: String, // URL, deep link path, or API endpoint
    val method: String = "GET", // HTTP method for API calls
    val payload: Map<String, String>? = null
)

data class NotificationMetadata(
    val deliveredAt: String? = null, // ISO format
    val readAt: String? = null, // ISO format
    val expiresAt: String? = null, // ISO format
    val priority: Int = 0,
    val category: String? = null,
    val tags: List<String> = emptyList()
)