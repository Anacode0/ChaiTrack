package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UpdateProfileRequest(
    @SerializedName("name")
    val name: String? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("plot_size")
    val plotSize: Double? = null,

    @SerializedName("farm_name")
    val farmName: String? = null,

    @SerializedName("collection_center")
    val collectionCenter: String? = null
)