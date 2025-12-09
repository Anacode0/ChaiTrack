package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ConfirmPickupResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: ConfirmPickupData?
)

data class ConfirmPickupData(
    @SerializedName("pickup_confirmation_id")
    val pickupConfirmationId: String,

    @SerializedName("completion_date")
    val completionDate: String,

    @SerializedName("next_application_eligibility")
    val nextApplicationEligibility: String?,

    @SerializedName("receipt_url")
    val receiptUrl: String?
)