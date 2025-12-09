package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CheckPhoneResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("is_registered")
    val isRegistered: Boolean,

    @SerializedName("user_exists")
    val userExists: Boolean
)