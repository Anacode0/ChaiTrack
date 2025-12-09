package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class FertilizerApplicationDetailResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: FertilizerApplicationDetailData?
)

data class FertilizerApplicationDetailData(
    @SerializedName("application")
    val application: FertilizerApplicationDto,

    @SerializedName("farmer_info")
    val farmerInfo: FarmerFertilizerInfo,

    @SerializedName("approval_details")
    val approvalDetails: ApprovalDetails?,

    @SerializedName("pickup_details")
    val pickupDetails: PickupDetails?
)

data class FarmerFertilizerInfo(
    @SerializedName("farmer_name")
    val farmerName: String,

    @SerializedName("farmer_id")
    val farmerId: String,

    @SerializedName("plot_size")
    val plotSize: Double,

    @SerializedName("collection_center")
    val collectionCenter: String,

    @SerializedName("tea_variety")
    val teaVariety: String?
)

data class ApprovalDetails(
    @SerializedName("approved_by")
    val approvedBy: String?,

    @SerializedName("approval_notes")
    val approvalNotes: String?,

    @SerializedName("approval_criteria")
    val approvalCriteria: List<String>?
)

data class PickupDetails(
    @SerializedName("pickup_location")
    val pickupLocation: String,

    @SerializedName("pickup_address")
    val pickupAddress: String,

    @SerializedName("contact_person")
    val contactPerson: String,

    @SerializedName("contact_phone")
    val contactPhone: String,

    @SerializedName("operating_hours")
    val operatingHours: String,

    @SerializedName("pickup_deadline")
    val pickupDeadline: String?
)