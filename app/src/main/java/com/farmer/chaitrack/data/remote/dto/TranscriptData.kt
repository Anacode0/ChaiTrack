package com.farmer.chaitrack.data.remote.dto

import java.time.LocalDate

/**
 * Data class representing transcript/salary slip data
 * Contains all necessary information for generating PDF transcripts
 */
data class TranscriptData(
    val transcriptId: String,
    val farmerId: String,
    val farmerName: String,
    val phoneNumber: String,

    val month: Int,
    val year: Int,
    val period: String,
    val generationDate: LocalDate,

    val farmName: String? = null,
    val plotSize: Double,
    val collectionCenter: String? = null,

    val totalDeliveries: Int,
    val totalKilos: Double,
    val pricePerKg: Double,

    val basicEarnings: Double,
    val bonuses: Double = 0.0,
    val deductions: Double = 0.0,
    val netEarnings: Double,

    val bankName: String? = null,
    val accountNumber: String? = null,
    val paymentStatus: String, // PENDING, PAID, PROCESSING
    val paymentDate: LocalDate? = null,

    val qualityBonus: Double = 0.0,
    val loyaltyBonus: Double = 0.0,
    val advanceDeductions: Double = 0.0,
    val otherDeductions: Double = 0.0,

    val isVerified: Boolean = false,
    val verificationDate: LocalDate? = null,
    val notes: String? = null
) {
    /**
     * Calculate gross earnings (basic + bonuses)
     */
    val grossEarnings: Double
        get() = basicEarnings + bonuses

    /**
     * Calculate total deductions
     */
    val totalDeductions: Double
        get() = deductions + advanceDeductions + otherDeductions

    /**
     * Get formatted period string
     */
    val formattedPeriod: String
        get() = "${getMonthName(month)} $year"

    /**
     * Get month name from month number
     */
    private fun getMonthName(month: Int): String {
        return when (month) {
            1 -> "January"
            2 -> "February"
            3 -> "March"
            4 -> "April"
            5 -> "May"
            6 -> "June"
            7 -> "July"
            8 -> "August"
            9 -> "September"
            10 -> "October"
            11 -> "November"
            12 -> "December"
            else -> "Unknown"
        }
    }

    companion object {
        /**
         * Create empty/default TranscriptData
         */
        fun empty(): TranscriptData {
            return TranscriptData(
                transcriptId = "",
                farmerId = "",
                farmerName = "",
                phoneNumber = "",
                month = 0,
                year = 0,
                period = "",
                generationDate = LocalDate.now(),
                plotSize = 0.0,
                totalDeliveries = 0,
                totalKilos = 0.0,
                pricePerKg = 0.0,
                basicEarnings = 0.0,
                netEarnings = 0.0,
                paymentStatus = "PENDING"
            )
        }
    }
}

/**
 * Extension function to check if transcript is paid
 */
val TranscriptData.isPaid: Boolean
    get() = paymentStatus.equals("PAID", ignoreCase = true)

/**
 * Extension function to check if transcript is pending payment
 */
val TranscriptData.isPending: Boolean
    get() = paymentStatus.equals("PENDING", ignoreCase = true)

/**
 * Extension function to get formatted earnings
 */
fun TranscriptData.getFormattedEarnings(): String {
    return "KSh ${"%.2f".format(netEarnings)}"
}

/**
 * Extension function to get formatted price per kg
 */
fun TranscriptData.getFormattedPricePerKg(): String {
    return "KSh ${"%.2f".format(pricePerKg)}"
}

/**
 * Extension function to calculate average delivery weight
 */
fun TranscriptData.getAverageDeliveryWeight(): Double {
    return if (totalDeliveries > 0) totalKilos / totalDeliveries else 0.0
}