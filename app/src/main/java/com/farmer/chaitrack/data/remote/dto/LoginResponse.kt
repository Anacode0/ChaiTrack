package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Response from login API
 */
data class LoginResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: LoginData?
)

data class LoginData(
    @SerializedName("user")
    val user: UserDto,

    @SerializedName("auth_token")
    val authToken: String,

    @SerializedName("refresh_token")
    val refreshToken: String,

    @SerializedName("token_type")
    val tokenType: String = "Bearer",

    @SerializedName("expires_in")
    val expiresIn: Long, // Token expiry time in seconds

    @SerializedName("is_first_login")
    val isFirstLogin: Boolean = false
)

data class UserDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("phone_number")
    val phoneNumber: String,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("plot_size")
    val plotSize: Double? = null,

    @SerializedName("farm_name")
    val farmName: String? = null,

    @SerializedName("collection_center")
    val collectionCenter: String? = null,

    @SerializedName("profile_image_url")
    val profileImageUrl: String? = null,

    @SerializedName("first_login_year")
    val firstLoginYear: Int? = null,

    @SerializedName("account_status")
    val accountStatus: String = "ACTIVE", // ACTIVE, SUSPENDED, INACTIVE

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null
)