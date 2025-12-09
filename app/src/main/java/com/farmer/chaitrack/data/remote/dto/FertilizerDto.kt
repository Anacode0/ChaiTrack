package com.farmer.chaitrack.data.remote.dto

/**
 * DTO for fertilizer application from server
 * Using FertilizerDto as the main data transfer object
 */
data class FertilizerDto(
    val id: String,
    val season: String,
    val plotSize: Double,
    val quantityRequested: Int,
    val quantityApproved: Int? = null,
    val recommendedQuantity: Int,
    val status: String,
    val applicationDate: String,
    val approvalDate: String? = null,
    val pickupDate: String? = null,
    val completionDate: String? = null,
    val approvedBy: String? = null,
    val approverId: String? = null,
    val rejectionReason: String? = null,
    val pickupLocation: String? = null,
    val notes: String? = null,
    val createdAt: String,
    val updatedAt: String
)