package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

/**
 * DTO for monthly earnings API responses
 */
data class MonthlyEarningsDto(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("data")
    val data: MonthlyEarningsData? = null,

    @SerializedName("error")
    val error: String? = null,

    @SerializedName("timestamp")
    val timestamp: Long? = null
) {
    /**
     * Check if response is successful and has data
     */
    fun isValid(): Boolean {
        return success && data != null
    }

    /**
     * Get total earnings safely
     */
    fun getTotalEarnings(): Double {
        return data?.totalEarnings ?: 0.0
    }

    /**
     * Get formatted month and year
     */
    fun getFormattedPeriod(): String {
        return data?.getFormattedPeriod() ?: "Unknown Period"
    }
}

/**
 * Monthly earnings data container
 */
data class MonthlyEarningsData(
    @SerializedName("year")
    val year: Int,

    @SerializedName("month")
    val month: Int,

    @SerializedName("totalEarnings")
    val totalEarnings: Double,

    @SerializedName("totalWeight")
    val totalWeight: Double,

    @SerializedName("pricePerKg")
    val pricePerKg: Double,

    @SerializedName("deliveryCount")
    val deliveryCount: Int,

    @SerializedName("netEarnings")
    val netEarnings: Double? = null,

    @SerializedName("deductions")
    val deductions: Double? = null,

    @SerializedName("deductionBreakdown")
    val deductionBreakdown: Map<String, Double>? = null,

    @SerializedName("paymentStatus")
    val paymentStatus: String? = null, // PENDING, PAID, PROCESSING

    @SerializedName("paymentDate")
    val paymentDate: String? = null,

    @SerializedName("farmerId")
    val farmerId: String? = null,

    @SerializedName("collectionCenter")
    val collectionCenter: String? = null,

    @SerializedName("grade")
    val grade: String? = null,

    @SerializedName("currency")
    val currency: String? = "INR",

    @SerializedName("createdAt")
    val createdAt: String? = null,

    @SerializedName("updatedAt")
    val updatedAt: String? = null,

    @SerializedName("deliveryBreakdown")
    val deliveryBreakdown: List<DeliveryBreakdown>? = null,

    @SerializedName("previousMonthComparison")
    val previousMonthComparison: EarningsComparison? = null,

    val pdfGenerated: Boolean = false,
    val pdfUrl: String = "",

    @SerializedName("sameMonthLastYear")
    val sameMonthLastYear: EarningsComparison? = null
) {
    /**
     * Get formatted month and year (e.g., "January 2024")
     */
    fun getFormattedPeriod(): String {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month - 1)
        }
        return SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(calendar.time)
    }

    /**
     * Get month name
     */
    fun getMonthName(): String {
        return SimpleDateFormat("MMMM", Locale.getDefault()).let { format ->
            val calendar = Calendar.getInstance().apply {
                set(Calendar.MONTH, month - 1)
            }
            format.format(calendar.time)
        }
    }

    /**
     * Calculate average earnings per delivery
     */
    fun getAveragePerDelivery(): Double {
        return if (deliveryCount != null && deliveryCount > 0) {
            totalEarnings / deliveryCount
        } else {
            0.0
        }
    }

    /**
     * Calculate average weight per delivery
     */
    fun getAverageWeightPerDelivery(): Double {
        return if (deliveryCount != null && deliveryCount > 0) {
            totalWeight / deliveryCount
        } else {
            0.0
        }
    }

    /**
     * Check if payment is completed
     */
    fun isPaid(): Boolean {
        return paymentStatus == "PAID"
    }

    /**
     * Check if payment is pending
     */
    fun isPending(): Boolean {
        return paymentStatus == "PENDING"
    }
}

/**
 * Individual delivery breakdown
 */
data class DeliveryBreakdown(
    @SerializedName("date")
    val date: String, // ISO date format

    @SerializedName("weight")
    val weight: Double,

    @SerializedName("earnings")
    val earnings: Double,

    @SerializedName("docket_number")
    val docketNumber: String?,

    @SerializedName("pricePerKg")
    val pricePerKg: Double,

    @SerializedName("grade")
    val grade: String? = null,

    @SerializedName("collectionCenter")
    val collectionCenter: String? = null,

    @SerializedName("deliveryId")
    val deliveryId: String? = null,

    @SerializedName("status")
    val status: String? = null // COMPLETED, PENDING, CANCELLED
)

/**
 * Earnings comparison data
 */
data class EarningsComparison(
    @SerializedName("year")
    val year: Int,

    @SerializedName("month")
    val month: Int,

    @SerializedName("totalEarnings")
    val totalEarnings: Double,

    @SerializedName("totalWeight")
    val totalWeight: Double,

    @SerializedName("percentageChange")
    val percentageChange: Double,

    @SerializedName("changeDirection")
    val changeDirection: String // INCREASE, DECREASE, SAME
)

/**
 * Request DTO for monthly earnings
 */
data class MonthlyEarningsRequestDto(
    @SerializedName("year")
    val year: Int,

    @SerializedName("month")
    val month: Int,

    @SerializedName("farmerId")
    val farmerId: String? = null,

    @SerializedName("collectionCenter")
    val collectionCenter: String? = null,

    @SerializedName("includeBreakdown")
    val includeBreakdown: Boolean? = false
)

/**
 * Request DTO for earnings period
 */
data class EarningsPeriodRequestDto(
    @SerializedName("startYear")
    val startYear: Int,

    @SerializedName("startMonth")
    val startMonth: Int,

    @SerializedName("endYear")
    val endYear: Int,

    @SerializedName("endMonth")
    val endMonth: Int,

    @SerializedName("farmerId")
    val farmerId: String? = null
)

/**
 * Multiple months earnings response
 */
data class MultipleMonthsEarningsDto(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: List<MonthlyEarningsData>? = null,

    @SerializedName("summary")
    val summary: EarningsSummary? = null
)

/**
 * Earnings summary for multiple months
 */
data class EarningsSummary(
    @SerializedName("totalEarnings")
    val totalEarnings: Double,

    @SerializedName("totalWeight")
    val totalWeight: Double,

    @SerializedName("averageEarnings")
    val averageEarnings: Double,

    @SerializedName("averagePricePerKg")
    val averagePricePerKg: Double,

    @SerializedName("totalDeliveryCount")
    val totalDeliveryCount: Int,

    @SerializedName("period")
    val period: String
)