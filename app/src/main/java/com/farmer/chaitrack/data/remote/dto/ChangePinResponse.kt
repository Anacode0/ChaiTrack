package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ChangePinResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String
)