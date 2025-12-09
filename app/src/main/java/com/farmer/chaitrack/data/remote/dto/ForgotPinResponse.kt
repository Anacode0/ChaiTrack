package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ForgotPinResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("recovery_method")
    val recoveryMethod: String // "SMS", "EMAIL"
)