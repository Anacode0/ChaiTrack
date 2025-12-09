package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PriceHistoryResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: PriceHistoryData?
)

data class PriceHistoryData(
    @SerializedName("price_history")
    val priceHistory: List<PriceRecord>,

    @SerializedName("current_price")
    val currentPrice: Double,

    @SerializedName("average_price")
    val averagePrice: Double,

    @SerializedName("price_trend")
    val priceTrend: String // "UP", "DOWN", "STABLE"
)

data class PriceRecord(
    @SerializedName("date")
    val date: String,

    @SerializedName("price_per_kg")
    val pricePerKg: Double,

    @SerializedName("grade")
    val grade: String,

    @SerializedName("effective_from")
    val effectiveFrom: String,

    @SerializedName("effective_to")
    val effectiveTo: String?
)