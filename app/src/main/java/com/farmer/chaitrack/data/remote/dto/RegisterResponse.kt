package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: RegistrationData?
)

data class RegistrationData(
    @SerializedName("user")
    val user: UserDto,

    @SerializedName("auth_token")
    val authToken: String,

    @SerializedName("refresh_token")
    val refreshToken: String,

    @SerializedName("temporary_pin")
    val temporaryPin: String // Initial PIN sent to user
)