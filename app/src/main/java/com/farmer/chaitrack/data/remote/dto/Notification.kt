package com.farmer.chaitrack.data.remote.dto

data class Notification(
    val id: String,
    val title: String,
    val message: String,
    val type: String,
    val isRead: Boolean,
    val createdAt: String,
    val data: Map<String, String>? = null,
    val actionUrl: String? = null,
    val priority: String = "normal"
)