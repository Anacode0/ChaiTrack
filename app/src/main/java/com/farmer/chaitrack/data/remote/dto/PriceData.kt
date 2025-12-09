package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PriceData(
    @SerializedName("year")
    val year: Int,

    @SerializedName("month")
    val month: Int,

    @SerializedName("price_per_kg")
    val pricePerKg: Double,

    @SerializedName("effective_date")
    val effectiveDate: String,

    @SerializedName("currentPrice")
    val currentPrice: Double? = null,

    @SerializedName("priceHistory")
    val priceHistory: List<PriceHistoryItemDto>? = null,

    @SerializedName("priceTrend")
    val priceTrend: PriceTrend? = null,

    @SerializedName("lastUpdated")
    val lastUpdated: String? = null,

    @SerializedName("currency")
    val currency: String? = "INR",

    @SerializedName("unit")
    val unit: String? = "kg",

    @SerializedName("grade")
    val grade: String? = null,

    @SerializedName("collectionCenter")
    val collectionCenter: String? = null
)