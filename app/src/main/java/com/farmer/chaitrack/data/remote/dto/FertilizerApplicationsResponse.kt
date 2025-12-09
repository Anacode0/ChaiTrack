package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class FertilizerApplicationsResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: FertilizerApplicationsData?
)

data class FertilizerApplicationsData(
    @SerializedName("applications")
    val applications: List<FertilizerApplicationDto>,

    @SerializedName("pagination")
    val pagination: PaginationInfo,

    @SerializedName("summary")
    val summary: FertilizerApplicationsSummary?
)

data class FertilizerApplicationDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("season")
    val season: String, // e.g., "Spring 2024"

    @SerializedName("plot_size")
    val plotSize: Double,

    @SerializedName("quantity_requested")
    val quantityRequested: Int, // in bags

    @SerializedName("quantity_approved")
    val quantityApproved: Int?,

    @SerializedName("status")
    val status: String, // "PENDING", "APPROVED", "READY_FOR_PICKUP", "REJECTED", "COMPLETED"

    @SerializedName("applied_date")
    val appliedDate: String,

    @SerializedName("approval_date")
    val approvalDate: String?,

    @SerializedName("pickup_date")
    val pickupDate: String?,

    @SerializedName("pickup_location")
    val pickupLocation: String?,

    @SerializedName("fertilizer_type")
    val fertilizerType: String,

    @SerializedName("recommended_quantity")
    val recommendedQuantity: Int,

    @SerializedName("rejection_reason")
    val rejectionReason: String?,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String?
)

data class FertilizerApplicationsSummary(
    @SerializedName("total_applications")
    val totalApplications: Int,

    @SerializedName("pending_count")
    val pendingCount: Int,

    @SerializedName("approved_count")
    val approvedCount: Int,

    @SerializedName("completed_count")
    val completedCount: Int,

    @SerializedName("rejected_count")
    val rejectedCount: Int
)