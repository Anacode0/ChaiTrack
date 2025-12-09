package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class FinancialSummaryResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: FinancialSummaryData?
)

data class FinancialSummaryData(
    @SerializedName("period")
    val period: PeriodInfo,

    @SerializedName("current_year")
    val currentYear: YearlySummary,

    @SerializedName("previous_year")
    val previousYear: YearlySummary?,

    @SerializedName("growth_metrics")
    val growthMetrics: GrowthMetrics,

    @SerializedName("monthly_trend")
    val monthlyTrend: List<MonthlyTrend>
)

data class YearlySummary(
    @SerializedName("year")
    val year: Int,

    @SerializedName("total_earnings")
    val totalEarnings: Double,

    @SerializedName("total_deductions")
    val totalDeductions: Double,

    @SerializedName("net_earnings")
    val netEarnings: Double,

    @SerializedName("delivery_count")
    val deliveryCount: Int,

    @SerializedName("average_monthly_earnings")
    val averageMonthlyEarnings: Double
)

data class GrowthMetrics(
    @SerializedName("earnings_growth")
    val earningsGrowth: Double,

    @SerializedName("delivery_growth")
    val deliveryGrowth: Double,

    @SerializedName("growth_direction")
    val growthDirection: String // "UP", "DOWN", "STABLE"
)

data class MonthlyTrend(
    @SerializedName("month")
    val month: String,

    @SerializedName("earnings")
    val earnings: Double,

    @SerializedName("deliveries")
    val deliveries: Int
)