package com.farmer.chaitrack.domain.model

import com.farmer.chaitrack.domain.usecase.notification.NotificationPriority
import com.farmer.chaitrack.domain.usecase.notification.NotificationType

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val type: NotificationType,
    val priority: NotificationPriority = NotificationPriority.MEDIUM,
    val timestamp: Long,
    val isRead: Boolean = false,
    val actionUrl: String? = null,
    val relatedId: String? = null,
    val imageUrl: String? = null
) {
    fun getFormattedTime(): String {
        val date = java.util.Date(timestamp)
        val format = java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault())
        return format.format(date)
    }

    fun getFormattedDate(): String {
        val date = java.util.Date(timestamp)
        val format = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault())
        return format.format(date)
    }

    fun isUrgent(): Boolean {
        return priority == NotificationPriority.URGENT
    }

    fun hasAction(): Boolean {
        return actionUrl != null || relatedId != null
    }
}