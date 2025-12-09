package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("name")
    val name: String,

    @SerializedName("phone_number")
    val phoneNumber: String,

    @SerializedName("plot_size")
    val plotSize: Double,

    @SerializedName("farm_name")
    val farmName: String,

    @SerializedName("collection_center")
    val collectionCenter: String
)