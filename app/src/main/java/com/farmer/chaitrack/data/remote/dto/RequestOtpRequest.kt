package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RequestOtpRequest(
    @SerializedName("phone_number")
    val phoneNumber: String,

    @SerializedName("purpose")
    val purpose: String
)