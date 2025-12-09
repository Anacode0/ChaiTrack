package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MonthlyDeliverySummaryResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: MonthlyDeliverySummaryData?
)

data class MonthlyDeliverySummaryData(
    @SerializedName("year")
    val year: Int,

    @SerializedName("month")
    val month: Int,

    @SerializedName("month_name")
    val monthName: String,

    @SerializedName("total_weight")
    val totalWeight: Double,

    @SerializedName("total_earnings")
    val totalEarnings: Double,

    val totalDeliveries: Int,

    @SerializedName("delivery_count")
    val deliveryCount: Int,

    @SerializedName("average_price_per_kg")
    val averagePricePerKg: Double,

    @SerializedName("daily_summaries")
    val dailySummaries: List<DailySummary>,

    @SerializedName("payment_status")
    val paymentStatus: String // "PENDING", "PROCESSED", "PAID"
)

data class DailySummary(
    @SerializedName("date")
    val date: String,

    @SerializedName("weight")
    val weight: Double,

    @SerializedName("earnings")
    val earnings: Double,

    @SerializedName("delivery_count")
    val deliveryCount: Int
)