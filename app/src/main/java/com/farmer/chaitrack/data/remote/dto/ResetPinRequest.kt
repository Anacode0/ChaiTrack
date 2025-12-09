package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ResetPinRequest(
    @SerializedName("verification_token")
    val verificationToken: String,

    @SerializedName("new_pin")
    val newPin: String
)