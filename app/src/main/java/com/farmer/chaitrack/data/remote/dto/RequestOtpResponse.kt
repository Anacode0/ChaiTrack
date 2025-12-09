package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RequestOtpResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("otp_reference")
    val otpReference: String,

    @SerializedName("expires_in")
    val expiresIn: Int // OTP expiry time in seconds
)