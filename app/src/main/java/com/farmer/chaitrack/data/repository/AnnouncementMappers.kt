package com.farmer.chaitrack.data.repository

import com.farmer.chaitrack.data.local.entity.AnnouncementEntity
import com.farmer.chaitrack.domain.model.Announcement
import java.text.SimpleDateFormat
import java.util.*

fun AnnouncementEntity.toDomainModel(): Announcement {
    return Announcement(
        id = this.id,
        title = this.title,
        body = this.body,
        content = this.body,
        category = this.category ?: "GENERAL",
        date = formatTimestampToIso(this.date),
        isUrgent = this.priority == "URGENT",
        isRead = this.isRead,
        imageUrl = this.imageUrl,
        author = this.author,
        expiresAt = this.expiresAt?.let { formatTimestampToIso(it) },
        isImportant = this.isPinned
    )
}

fun Announcement.toEntity(userId: String? = null): AnnouncementEntity {
    return AnnouncementEntity(
        id = this.id,
        userId = userId,
        title = this.title,
        body = this.content,
        summary = this.content.take(100),
        date = parseIsoToTimestamp(this.date),
        isRead = this.isRead,
        isPinned = this.isImportant,
        priority = if (this.isUrgent) "URGENT" else "NORMAL",
        category = this.category,
        author = this.author,
        imageUrl = this.imageUrl,
        expiresAt = this.expiresAt?.let { parseIsoToTimestamp(it) },
        createdAt = parseIsoToTimestamp(this.date),
        updatedAt = System.currentTimeMillis()
    )
}

private fun formatTimestampToIso(timestamp: Long): String {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    return format.format(Date(timestamp))
}

private fun parseIsoToTimestamp(isoString: String): Long {
    return try {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        format.parse(isoString)?.time ?: System.currentTimeMillis()
    } catch (e: Exception) {
        System.currentTimeMillis()
    }
}