package com.farmer.chaitrack.domain.model

data class FertilizerRecord(
    val id: String,
    val season: String,
    val year: Int,
    val plotSizeAcres: Double,
    val quantityRequestedBags: Int,
    val quantityApprovedBags: Int?,
    val status: FertilizerStatus,
    val applicationDate: Long,
    val reviewedDate: Long?,
    val approvedDate: Long?,
    val pickupDate: Long?,
    val notes: String?,
    val rejectionReason: String?
) {
    fun isApproved(): Boolean {
        return status == FertilizerStatus.APPROVED ||
                status == FertilizerStatus.READY_FOR_PICKUP ||
                status == FertilizerStatus.PICKED_UP
    }

    fun isReadyForPickup(): Boolean {
        return status == FertilizerStatus.READY_FOR_PICKUP
    }

    fun isPending(): Boolean {
        return status == FertilizerStatus.PENDING_REVIEW
    }

    fun isRejected(): Boolean {
        return status == FertilizerStatus.REJECTED
    }

    fun getStatusDisplayText(): String {
        return when (status) {
            FertilizerStatus.PENDING_REVIEW -> "Pending Review"
            FertilizerStatus.UNDER_REVIEW -> "Under Review"
            FertilizerStatus.APPROVED -> "Approved"
            FertilizerStatus.READY_FOR_PICKUP -> "Ready for Pickup"
            FertilizerStatus.PICKED_UP -> "Picked Up"
            FertilizerStatus.REJECTED -> "Rejected"
        }
    }

    fun getProgressPercentage(): Int {
        return when (status) {
            FertilizerStatus.PENDING_REVIEW -> 0
            FertilizerStatus.UNDER_REVIEW -> 25
            FertilizerStatus.APPROVED -> 50
            FertilizerStatus.READY_FOR_PICKUP -> 75
            FertilizerStatus.PICKED_UP -> 100
            FertilizerStatus.REJECTED -> 0
        }
    }

    fun getSeasonDisplayText(): String {
        return "$season $year"
    }
}

enum class FertilizerStatus {
    PENDING_REVIEW,
    UNDER_REVIEW,
    APPROVED,
    READY_FOR_PICKUP,
    PICKED_UP,
    REJECTED
}

enum class FertilizerSeason(val displayName: String) {
    SPRING("Spring"),
    SUMMER("Summer"),
    FALL("Fall"),
    WINTER("Winter");

    companion object {
        fun fromString(value: String): FertilizerSeason {
            return values().find {
                it.name.equals(value, ignoreCase = true) ||
                        it.displayName.equals(value, ignoreCase = true)
            } ?: SPRING
        }
    }
}