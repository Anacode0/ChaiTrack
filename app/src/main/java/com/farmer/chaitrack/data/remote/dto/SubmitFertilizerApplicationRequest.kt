package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SubmitFertilizerApplicationRequest(
    @SerializedName("season")
    val season: String,

    @SerializedName("plot_size")
    val plotSize: Double,

    @SerializedName("quantity_requested")
    val quantityRequested: Int,

    @SerializedName("fertilizer_type")
    val fertilizerType: String,

    @SerializedName("preferred_pickup_location")
    val preferredPickupLocation: String? = null,

    @SerializedName("notes")
    val notes: String? = null
)