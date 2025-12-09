package com.farmer.chaitrack.data.remote.dto

import java.text.SimpleDateFormat
import java.util.*

// DTO for financial summary/overview data
data class FinancialSummaryDto(
    // Time period identification
    val year: Int?,
    val month: Int?,
    val periodLabel: String?, // e.g., "October 2025", "Q4 2025", "Annual 2025"

    // Aggregated financial metrics
    val totalWeight: Double,
    val totalEarnings: Double,
    val totalNetEarnings: Double?,
    val totalDeductions: Double?,

    // Delivery statistics
    val totalDeliveryCount: Int?,
    val averageDeliveryWeight: Double?,
    val averagePricePerKg: Double,

    // Payment summary
    val paidAmount: Double?,
    val pendingAmount: Double?,
    val paymentStatusBreakdown: Map<String, Int>?, // e.g., "PENDING" to 5, "PAID" to 3

    // PDF/document summary
    val pdfGeneratedCount: Int?,
    val pdfSharedCount: Int?,

    // Date ranges
    val periodStart: String?, // ISO date string
    val periodEnd: String?,   // ISO date string

    // Comparative data (optional - for trends)
    val previousPeriodEarnings: Double?,
    val earningsGrowth: Double?, // Percentage
    val weightGrowth: Double?,   // Percentage

    // Additional metadata
    val currency: String?,
    val lastUpdated: String?,

    // Detailed breakdown (optional)
    val monthlyBreakdown: List<MonthlyFinancialData>?,
    val topDeductionReasons: Map<String, Double>? // Reason to total amount
) {
    // Helper data class for monthly breakdown
    data class MonthlyBreakdown(
        val year: Int,
        val month: Int,
        val monthName: String,
        val totalEarnings: Double,
        val totalWeight: Double,
        val deliveryCount: Int,
        val paymentStatus: String
    )

    data class MonthlyFinancialData(
        val month: Int,
        val year: Int,
        val earnings: Double,
        val weight: Double
    )
}

// Extension functions for FinancialSummaryDto
fun FinancialSummaryDto.getFormattedPeriod(): String {
    return periodLabel ?: if (month != null && year != null) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month - 1)
        }
        SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(calendar.time)
    } else if (year != null) {
        "Year $year"
    } else {
        "Unknown Period"
    }
}

fun FinancialSummaryDto.getEarningsGrowthFormatted(): String {
    return if (earningsGrowth != null) {
        String.format("%.1f%%", earningsGrowth)
    } else {
        "N/A"
    }
}

fun FinancialSummaryDto.getGrowthIndicator(): String {
    return when {
        earningsGrowth == null -> "→"
        earningsGrowth > 0 -> "↑"
        earningsGrowth < 0 -> "↓"
        else -> "→"
    }
}

// Helper function to convert from list of FinanceRecordDto to FinancialSummaryDto
fun List<FinanceRecordDto>.toFinancialSummaryDto(
    year: Int? = null,
    month: Int? = null,
    periodLabel: String? = null
): FinancialSummaryDto {
    val filteredRecords = if (year != null) {
        if (month != null) {
            this.filter { it.year == year && it.month == month }
        } else {
            this.filter { it.year == year }
        }
    } else {
        this
    }

    val totalWeight = filteredRecords.sumOf { it.totalWeight ?: 0.0 }
    val totalEarnings = filteredRecords.sumOf { it.totalEarnings ?: 0.0 }
    val totalNetEarnings = filteredRecords.sumOf { it.netEarnings ?: 0.0 }
    val totalDeductions = filteredRecords.sumOf { it.deductions ?: 0.0 }
    val totalDeliveryCount = filteredRecords.sumOf { it.deliveryCount ?: 0 }

    val paidRecords = filteredRecords.filter { it.paymentStatus == "PAID" }
    val paidAmount = paidRecords.sumOf { it.netEarnings ?: 0.0 }
    val pendingAmount = totalNetEarnings - paidAmount

    val paymentStatusBreakdown = filteredRecords
        .groupingBy { it.paymentStatus ?: "UNKNOWN" }
        .eachCount()

    // FIXED: Use the correct property names from FinanceRecordDto
    val pdfGeneratedCount = filteredRecords.count { it.pdfGenerated == true }
    val pdfSharedCount = filteredRecords.count { it.pdfShared == true }

    val averageDeliveryWeight = if (totalDeliveryCount > 0) {
        totalWeight / totalDeliveryCount
    } else {
        0.0
    }

    // FIXED: Calculate averagePricePerKg
    val averagePricePerKg = if (totalWeight > 0) {
        totalEarnings / totalWeight
    } else {
        0.0
    }

    // FIXED: Corrected top deduction reasons calculation
    val topDeductionReasons = calculateTopDeductionReasons(filteredRecords)

    return FinancialSummaryDto(
        year = year,
        month = month,
        periodLabel = periodLabel,
        totalWeight = totalWeight,
        totalEarnings = totalEarnings,
        totalNetEarnings = totalNetEarnings,
        totalDeductions = totalDeductions,
        totalDeliveryCount = totalDeliveryCount,
        averageDeliveryWeight = averageDeliveryWeight,
        averagePricePerKg = averagePricePerKg, // FIXED: Now properly calculated
        paidAmount = paidAmount,
        pendingAmount = pendingAmount,
        paymentStatusBreakdown = paymentStatusBreakdown,
        pdfGeneratedCount = pdfGeneratedCount,
        pdfSharedCount = pdfSharedCount,
        periodStart = null, // Could be calculated from actual dates
        periodEnd = null,   // Could be calculated from actual dates
        previousPeriodEarnings = null, // Would require additional data
        earningsGrowth = null,         // Would require additional data
        weightGrowth = null,           // Would require additional data
        currency = "INR", // Default currency, could be parameterized
        lastUpdated = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(Date()),
        monthlyBreakdown = null, // Could be populated for annual summaries
        topDeductionReasons = topDeductionReasons
    )
}

// FIXED: Separate function to calculate top deduction reasons with correct property name
private fun calculateTopDeductionReasons(records: List<FinanceRecordDto>): Map<String, Double> {
    val deductionMap = mutableMapOf<String, Double>()

    records.forEach { record ->
        val reason = record.deductionReason ?: "Other"
        val amount = record.deductions ?: 0.0

        if (amount > 0) {
            val currentTotal = deductionMap[reason] ?: 0.0
            deductionMap[reason] = currentTotal + amount
        }
    }

    // Return top 5 deduction reasons sorted by amount (descending)
    return deductionMap.toList()
        .sortedByDescending { (_, amount) -> amount }
        .take(5)
        .toMap()
}

// Extension function for single year summary
fun List<FinanceRecordDto>.toYearlySummary(year: Int): FinancialSummaryDto {
    return this.toFinancialSummaryDto(
        year = year,
        periodLabel = "Year $year"
    )
}

// Extension function for monthly summary
fun List<FinanceRecordDto>.toMonthlySummary(year: Int, month: Int): FinancialSummaryDto {
    val monthName = SimpleDateFormat("MMMM", Locale.getDefault()).let { format ->
        val calendar = Calendar.getInstance().apply {
            set(Calendar.MONTH, month - 1)
        }
        format.format(calendar.time)
    }

    return this.toFinancialSummaryDto(
        year = year,
        month = month,
        periodLabel = "$monthName $year"
    )
}