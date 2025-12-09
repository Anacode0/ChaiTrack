package com.farmer.chaitrack.domain.model

import com.farmer.chaitrack.ui.fertilizer.ApplicationStatus

data class FertilizerApplication(
    val id: String,
    val season: String,
    val plotSize: Double,
    val quantityRequested: Int,
    val quantityApproved: Int,
    val recommendedQuantity: Int,
    val status: ApplicationStatus,
    val applicationDate: Long,
    val approvalDate: Long? = null,
    val pickupDate: Long? = null,
    val completionDate: Long? = null,
    val approvedBy: String? = null,
    val approverId: String? = null,
    val rejectionReason: String? = null,
    val pickupLocation: String? = null,
    val notes: String? = null
) {
    constructor(
        id: String,
        season: String,
        plotSize: Double,
        quantityRequested: Int,
        quantityApproved: Int,
        recommendedQuantity: Int,
        status: ApplicationStatus,
        applicationDate: Long
    ) : this(
        id = id,
        season = season,
        plotSize = plotSize,
        quantityRequested = quantityRequested,
        quantityApproved = quantityApproved,
        recommendedQuantity = recommendedQuantity,
        status = status,
        applicationDate = applicationDate,
        approvalDate = null,
        pickupDate = null,
        completionDate = null,
        approvedBy = null,
        approverId = null,
        rejectionReason = null,
        pickupLocation = null,
        notes = null
    )
}