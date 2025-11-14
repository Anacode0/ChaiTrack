package com.farmer.chaitrack.ui.fertilizer


//Enum representing the status of a fertilizer application
enum class ApplicationStatus {
    PENDING_REVIEW,
    UNDER_REVIEW,
    APPROVED,
    READY_FOR_PICKUP,
    PICKED_UP,
    REJECTED,
    COMPLETED;

    companion object {

         // Get display text for status
        fun getDisplayText(status: ApplicationStatus): String {
            return when (status) {
                PENDING_REVIEW -> "Pending Review"
                UNDER_REVIEW -> "Under Review"
                APPROVED -> "Approved"
                READY_FOR_PICKUP -> "Ready for Pickup"
                PICKED_UP -> "Picked Up"
                REJECTED -> "Rejected"
                COMPLETED -> "Completed"
            }
        }

        // Check if status is active (not completed or rejected)
        fun isActive(status: ApplicationStatus): Boolean {
            return status !in listOf(COMPLETED, REJECTED)
        }

        //Check if status allows pickup confirmation
        fun canConfirmPickup(status: ApplicationStatus): Boolean {
            return status == READY_FOR_PICKUP
        }
    }
}