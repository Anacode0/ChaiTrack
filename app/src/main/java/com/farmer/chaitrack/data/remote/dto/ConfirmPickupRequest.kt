package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ConfirmPickupRequest(
    @SerializedName("pickup_date")
    val pickupDate: String,

    @SerializedName("pickup_time")
    val pickupTime: String,

    @SerializedName("received_quantity")
    val receivedQuantity: Int,

    @SerializedName("condition_rating")
    val conditionRating: Int,

    @SerializedName("feedback")
    val feedback: String? = null
)