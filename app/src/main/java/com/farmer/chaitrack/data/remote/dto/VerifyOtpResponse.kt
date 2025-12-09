package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class VerifyOtpResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("verification_token")
    val verificationToken: String?,

    @SerializedName("is_verified")
    val isVerified: Boolean
)