package com.farmer.chaitrack.domain.usecase.notification

enum class NotificationPriority(val level: Int, val displayName: String) {
    LOW(1, "Low"),
    MEDIUM(2, "Medium"),
    HIGH(3, "High"),
    URGENT(4, "Urgent");

    fun compareByLevel(other: NotificationPriority): Int {
        return this.level.compareTo(other.level)
    }

    companion object {
        fun fromLevel(level: Int): NotificationPriority {
            return values().find { it.level == level } ?: MEDIUM
        }
    }
}