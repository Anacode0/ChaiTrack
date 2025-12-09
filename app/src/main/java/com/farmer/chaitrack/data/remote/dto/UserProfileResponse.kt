package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserProfileResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: UserProfileData?
)

data class UserProfileData(
    @SerializedName("user")
    val user: UserDto,

    @SerializedName("statistics")
    val statistics: UserStatistics?
)

data class UserStatistics(
    @SerializedName("total_deliveries")
    val totalDeliveries: Int,

    @SerializedName("total_earnings")
    val totalEarnings: Double,

    @SerializedName("current_month_deliveries")
    val currentMonthDeliveries: Double,

    @SerializedName("current_month_earnings")
    val currentMonthEarnings: Double
)