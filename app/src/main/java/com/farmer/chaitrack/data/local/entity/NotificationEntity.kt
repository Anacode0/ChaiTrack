package com.farmer.chaitrack.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Index

@Entity(
    tableName = "notifications",
    indices = [
        Index(value = ["timestamp"], name = "index_notifications_timestamp"),
        Index(value = ["isRead"], name = "index_notifications_isRead"),
        Index(value = ["type"], name = "index_notifications_type"),
        Index(value = ["userId"], name = "index_notifications_userId")
    ]
)
data class NotificationEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "userId")
    val userId: String,

    @ColumnInfo(name = "type")
    val type: String, // DELIVERY, FERTILIZER, FINANCE, ANNOUNCEMENT, SYSTEM

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "message")
    val message: String,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long,

    @ColumnInfo(name = "isRead")
    val isRead: Boolean = false,

    @ColumnInfo(name = "readAt")
    val readAt: Long? = null,

    @ColumnInfo(name = "priority")
    val priority: String = "NORMAL",

    @ColumnInfo(name = "actionType")
    val actionType: String? = null,

    @ColumnInfo(name = "actionData")
    val actionData: String? = null,

    @ColumnInfo(name = "iconUrl")
    val iconUrl: String? = null,

    @ColumnInfo(name = "imageUrl")
    val imageUrl: String? = null,

    @ColumnInfo(name = "category")
    val category: String? = null,

    @ColumnInfo(name = "expiresAt")
    val expiresAt: Long? = null,

    @ColumnInfo(name = "soundEnabled")
    val soundEnabled: Boolean = true,

    @ColumnInfo(name = "vibrationEnabled")
    val vibrationEnabled: Boolean = true,

    @ColumnInfo(name = "groupKey")
    val groupKey: String? = null,

    @ColumnInfo(name = "createdAt")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "updatedAt")
    val updatedAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "syncedToServer")
    val syncedToServer: Boolean = false,

    @ColumnInfo(name = "serverId")
    val serverId: String? = null
)