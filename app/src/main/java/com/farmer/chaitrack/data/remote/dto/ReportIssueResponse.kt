package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ReportIssueResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: ReportIssueData?
)

data class ReportIssueData(
    @SerializedName("issue_id")
    val issueId: String,

    @SerializedName("reference_number")
    val referenceNumber: String,

    @SerializedName("status")
    val status: String, // "PENDING", "UNDER_REVIEW", "RESOLVED"

    @SerializedName("estimated_resolution_time")
    val estimatedResolutionTime: String? // e.g., "24-48 hours"
)