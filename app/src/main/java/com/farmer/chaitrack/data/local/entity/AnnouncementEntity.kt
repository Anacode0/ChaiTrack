package com.farmer.chaitrack.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Index

@Entity(
    tableName = "announcements",
    indices = [
        Index(value = ["date"], name = "index_announcements_date"),
        Index(value = ["isRead"], name = "index_announcements_isRead"),
        Index(value = ["isPinned"], name = "index_announcements_isPinned"),
        Index(value = ["userId"], name = "index_announcements_userId")
    ]
)
data class AnnouncementEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "userId")
    val userId: String? = null,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "body")
    val body: String,

    @ColumnInfo(name = "summary")
    val summary: String? = null,

    @ColumnInfo(name = "date")
    val date: Long,

    @ColumnInfo(name = "isRead")
    val isRead: Boolean = false,

    @ColumnInfo(name = "readAt")
    val readAt: Long? = null,

    @ColumnInfo(name = "isPinned")
    val isPinned: Boolean = false,

    @ColumnInfo(name = "priority")
    val priority: String = "NORMAL",

    @ColumnInfo(name = "category")
    val category: String? = null,

    @ColumnInfo(name = "author")
    val author: String? = null,

    @ColumnInfo(name = "authorRole")
    val authorRole: String? = null,

    @ColumnInfo(name = "imageUrl")
    val imageUrl: String? = null,

    @ColumnInfo(name = "attachmentUrl")
    val attachmentUrl: String? = null,

    @ColumnInfo(name = "attachmentName")
    val attachmentName: String? = null,

    @ColumnInfo(name = "actionUrl")
    val actionUrl: String? = null,

    @ColumnInfo(name = "actionText")
    val actionText: String? = null,

    @ColumnInfo(name = "expiresAt")
    val expiresAt: Long? = null,

    @ColumnInfo(name = "startsAt")
    val startsAt: Long? = null,

    @ColumnInfo(name = "viewCount")
    val viewCount: Int = 0,

    @ColumnInfo(name = "lastViewedAt")
    val lastViewedAt: Long? = null,

    @ColumnInfo(name = "tags")
    val tags: String? = null,

    @ColumnInfo(name = "isPublished")
    val isPublished: Boolean = true,

    @ColumnInfo(name = "createdAt")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "updatedAt")
    val updatedAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "syncedToServer")
    val syncedToServer: Boolean = false,

    @ColumnInfo(name = "serverId")
    val serverId: String? = null
)