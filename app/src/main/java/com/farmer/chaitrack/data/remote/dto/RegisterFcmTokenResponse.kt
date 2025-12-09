package com.farmer.chaitrack.data.remote.dto

data class RegisterFcmTokenResponse(
    val success: Boolean,
    val message: String,
    val registeredToken: String
)