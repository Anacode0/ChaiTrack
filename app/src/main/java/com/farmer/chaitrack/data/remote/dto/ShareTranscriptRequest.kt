package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ShareTranscriptRequest(
    @SerializedName("email_addresses")
    val emailAddresses: List<String> = emptyList(),

    @SerializedName("message")
    val message: String? ="",

    val deliveryId: String,

    val email: String,

    @SerializedName("subject")
    val subject: String? = null
)