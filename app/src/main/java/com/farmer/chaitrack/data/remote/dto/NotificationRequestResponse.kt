package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class NotificationRequestResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: NotificationRequestData?
)

data class NotificationRequestData(
    @SerializedName("notification_scheduled")
    val notificationScheduled: Boolean,

    @SerializedName("notification_type")
    val notificationType: String,

    @SerializedName("expected_notification_time")
    val expectedNotificationTime: String?,

    @SerializedName("contact_methods")
    val contactMethods: List<String> // "SMS", "PUSH", "EMAIL"
)