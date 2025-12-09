package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ApplicationsOpenResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: ApplicationsOpenData?
)

data class ApplicationsOpenData(
    @SerializedName("is_open")
    val isOpen: Boolean,

    @SerializedName("reason")
    val reason: String?,

    @SerializedName("opens_at")
    val opensAt: String?,

    @SerializedName("closes_at")
    val closesAt: String?,

    @SerializedName("current_season")
    val currentSeason: String?
)