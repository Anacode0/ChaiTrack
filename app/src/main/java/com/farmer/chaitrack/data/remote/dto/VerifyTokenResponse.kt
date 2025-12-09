package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class VerifyTokenResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: TokenVerificationData?
)

data class TokenVerificationData(
    @SerializedName("is_valid")
    val isValid: Boolean,

    @SerializedName("user_id")
    val userId: String,

    @SerializedName("expires_at")
    val expiresAt: String
)