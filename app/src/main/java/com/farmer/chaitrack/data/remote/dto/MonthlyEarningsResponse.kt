package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MonthlyEarningsResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: MonthlyEarningsData?
)

data class DailyEarning(
    @SerializedName("date")
    val date: String,

    @SerializedName("earnings")
    val earnings: Double,

    @SerializedName("weight")
    val weight: Double,

    @SerializedName("deliveries")
    val deliveries: Int
)

data class WeeklySummary(
    @SerializedName("week")
    val week: Int,

    @SerializedName("earnings")
    val earnings: Double,

    @SerializedName("percentage_of_month")
    val percentageOfMonth: Double
)

data class MonthlyComparison(
    @SerializedName("previous_month")
    val previousMonth: Double,

    @SerializedName("same_month_last_year")
    val sameMonthLastYear: Double,

    @SerializedName("growth_rate")
    val growthRate: Double
)