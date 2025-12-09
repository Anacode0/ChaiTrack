package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class VerifyOtpRequest(
    @SerializedName("phone_number")
    val phoneNumber: String,

    @SerializedName("otp_code")
    val otpCode: String,

    @SerializedName("otp_reference")
    val otpReference: String
)