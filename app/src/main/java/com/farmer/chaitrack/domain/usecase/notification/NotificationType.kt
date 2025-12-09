package com.farmer.chaitrack.domain.usecase.notification

enum class NotificationType(val displayName: String) {
    DELIVERY("Delivery"),
    FERTILIZER("Fertilizer"),
    FINANCE("Finance"),
    SYSTEM("System"),
    ANNOUNCEMENT("Announcement"),
    REMINDER("Reminder"),
    GENERAL("General");

    companion object {
        fun fromString(type: String): NotificationType? {
            return values().find { it.name.equals(type, ignoreCase = true) }
        }
    }
}