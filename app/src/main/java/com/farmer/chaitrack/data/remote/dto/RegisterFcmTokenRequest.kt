package com.farmer.chaitrack.data.remote.dto

data class RegisterFcmTokenRequest(
    val fcmToken: String,
    val deviceId: String,
    val deviceType: String = "android"
)