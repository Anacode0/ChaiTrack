package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SubmitFertilizerApplicationResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: SubmitApplicationData?
)

data class SubmitApplicationData(
    @SerializedName("application")
    val application: FertilizerApplicationDto,

    @SerializedName("application_id")
    val applicationId: String,

    @SerializedName("reference_number")
    val referenceNumber: String,

    @SerializedName("estimated_approval_time")
    val estimatedApprovalTime: String,

    @SerializedName("next_steps")
    val nextSteps: List<String>
)