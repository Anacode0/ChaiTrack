package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RefreshTokenResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: RefreshTokenData?
)

data class RefreshTokenData(
    @SerializedName("auth_token")
    val authToken: String,

    @SerializedName("refresh_token")
    val refreshToken: String,

    @SerializedName("token_type")
    val tokenType: String = "Bearer",

    @SerializedName("expires_in")
    val expiresIn: Long
)