package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CheckPhoneRequest(
    @SerializedName("phone_number")
    val phoneNumber: String
)