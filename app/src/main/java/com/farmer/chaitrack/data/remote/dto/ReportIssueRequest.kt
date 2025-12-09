package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ReportIssueRequest(
    @SerializedName("delivery_id")
    val deliveryId: String? = null,

    @SerializedName("issue_type")
    val issueType: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("suggested_resolution")
    val suggestedResolution: String? = null,

    @SerializedName("attachments")
    val attachments: List<String>? = null
)