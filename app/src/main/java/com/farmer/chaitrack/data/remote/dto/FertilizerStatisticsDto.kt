package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class FertilizerStatisticsDto(
    @SerializedName("total_applications")
    val totalApplications: Int,

    @SerializedName("pending_applications")
    val pendingApplications: Int,

    @SerializedName("approved_applications")
    val approvedApplications: Int,

    @SerializedName("rejected_applications")
    val rejectedApplications: Int,

    @SerializedName("total_quantity_requested")
    val totalQuantityRequested: Int,

    @SerializedName("total_quantity_approved")
    val totalQuantityApproved: Int,

    @SerializedName("completion_rate")
    val completionRate: Double?
)