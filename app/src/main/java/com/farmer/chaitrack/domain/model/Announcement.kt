package com.farmer.chaitrack.domain.model

import java.util.Date

data class Announcement(
    val id: String,
    val title: String,
    val content: String,
    val body: String,
    val category: String,
    val date: String, // ISO format
    val isUrgent: Boolean,
    val isRead: Boolean,
    val imageUrl: String? = null,
    val author: String? = null,
    val expiresAt: String? = null,
    val isImportant: Boolean = false
)