package com.farmer.chaitrack.data.remote.dto

data class Announcement(
    val id: String,
    val title: String,
    val content: String,
    val category: String,
    val date: String,
    val isUrgent: Boolean,
    val isRead: Boolean,
    val imageUrl: String? = null,
    val author: String? = null,
    val expiresAt: String? = null
)