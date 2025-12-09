package com.farmer.chaitrack.data.remote.dto

data class UpdateNotificationPreferencesRequest(
    val deliveryNotifications: Boolean? = null,
    val fertilizerNotifications: Boolean? = null,
    val financeNotifications: Boolean? = null,
    val announcementNotifications: Boolean? = null,
    val pushNotifications: Boolean? = null,
    val emailNotifications: Boolean? = null,
    val smsNotifications: Boolean? = null,
    val notificationsEnabled: Boolean,
    val soundEnabled: Boolean,
    val vibrationEnabled: Boolean
)