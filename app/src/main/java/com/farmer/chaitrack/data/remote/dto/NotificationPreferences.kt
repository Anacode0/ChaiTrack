package com.farmer.chaitrack.data.remote.dto

data class NotificationPreferences(
    val deliveryNotifications: Boolean = true,
    val fertilizerNotifications: Boolean = true,
    val financeNotifications: Boolean = true,
    val announcementNotifications: Boolean = true,
    val pushNotifications: Boolean = true,
    val emailNotifications: Boolean = false,
    val smsNotifications: Boolean = false
)