package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Request body for user login
 */
data class LoginRequest(
    @SerializedName("phone_number")
    val phoneNumber: String,

    @SerializedName("pin")
    val pin: String,

    @SerializedName("device_id")
    val deviceId: String? = null,

    @SerializedName("device_name")
    val deviceName: String? = null,

    @SerializedName("fcm_token")
    val fcmToken: String? = null
)