package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class DeliveryStatisticsResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: DeliveryStatisticsData?
)

data class PeriodInfo(
    @SerializedName("start_date")
    val startDate: String,

    @SerializedName("end_date")
    val endDate: String,

    @SerializedName("period_type")
    val periodType: String // "CUSTOM", "MONTH", "YEAR", "WEEK"
)

data class DeliveryStats(
    @SerializedName("total_deliveries")
    val totalDeliveries: Int,

    @SerializedName("total_weight")
    val totalWeight: Double,

    @SerializedName("total_earnings")
    val totalEarnings: Double,

    @SerializedName("average_daily_weight")
    val averageDailyWeight: Double,

    @SerializedName("best_day_weight")
    val bestDayWeight: Double,

    @SerializedName("best_day_date")
    val bestDayDate: String?
)

data class MonthlyStats(
    @SerializedName("year")
    val year: Int,

    @SerializedName("month")
    val month: Int,

    @SerializedName("month_name")
    val monthName: String,

    @SerializedName("delivery_count")
    val deliveryCount: Int,

    @SerializedName("total_weight")
    val totalWeight: Double,

    @SerializedName("total_earnings")
    val totalEarnings: Double
)

data class ComparisonStats(
    @SerializedName("previous_period")
    val previousPeriod: DeliveryStats,

    @SerializedName("growth_percentage")
    val growthPercentage: Double,

    @SerializedName("trend")
    val trend: String // "UP", "DOWN", "STABLE"
)