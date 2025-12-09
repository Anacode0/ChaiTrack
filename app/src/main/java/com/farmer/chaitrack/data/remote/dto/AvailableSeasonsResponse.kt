package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

//DTO for available seasons API response
data class AvailableSeasonsResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("data")
    val data: AvailableSeasonsData?
)

/**
 * Data wrapper for available seasons
 */
data class AvailableSeasonsData(
    @SerializedName("seasons")
    val seasons: List<String>
)