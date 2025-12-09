package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UploadImageResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("image_url")
    val imageUrl: String
)