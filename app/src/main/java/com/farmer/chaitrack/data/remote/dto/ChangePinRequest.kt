package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ChangePinRequest(
    @SerializedName("current_pin")
    val currentPin: String,

    @SerializedName("new_pin")
    val newPin: String
)