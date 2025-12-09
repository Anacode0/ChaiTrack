package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ShareTranscriptResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: ShareTranscriptData?
)

data class ShareTranscriptData(
    @SerializedName("shared_with")
    val sharedWith: List<String>,

    @SerializedName("failed_emails")
    val failedEmails: List<String>,

    @SerializedName("share_reference")
    val shareReference: String
)