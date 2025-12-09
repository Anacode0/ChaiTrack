package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class YearlyEarningsResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: YearlyEarningsData?
)

data class YearlyEarningsData(
    @SerializedName("year")
    val year: Int,

    @SerializedName("total_earnings")
    val totalEarnings: Double,

    @SerializedName("monthly_breakdown")
    val monthlyBreakdown: List<MonthlyEarning>,

    @SerializedName("quarterly_summary")
    val quarterlySummary: List<QuarterlySummary>
)

data class MonthlyEarning(
    @SerializedName("month")
    val month: String,

    @SerializedName("earnings")
    val earnings: Double,

    @SerializedName("weight")
    val weight: Double,

    @SerializedName("delivery_count")
    val deliveryCount: Int
)

data class QuarterlySummary(
    @SerializedName("quarter")
    val quarter: String,

    @SerializedName("earnings")
    val earnings: Double,

    @SerializedName("percentage_of_year")
    val percentageOfYear: Double
)