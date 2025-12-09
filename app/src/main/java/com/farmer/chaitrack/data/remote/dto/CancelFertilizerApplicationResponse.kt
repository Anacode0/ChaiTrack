package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CancelFertilizerApplicationResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: CancelApplicationData?
)

data class CancelApplicationData(
    @SerializedName("cancelled_application_id")
    val cancelledApplicationId: String,

    @SerializedName("cancellation_fee")
    val cancellationFee: Double?,

    @SerializedName("can_reapply")
    val canReapply: Boolean,

    @SerializedName("reapply_after")
    val reapplyAfter: String?
)