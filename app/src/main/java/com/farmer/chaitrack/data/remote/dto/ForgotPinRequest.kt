package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ForgotPinRequest(
    @SerializedName("phone_number")
    val phoneNumber: String
)