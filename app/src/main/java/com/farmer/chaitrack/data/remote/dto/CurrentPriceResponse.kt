package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CurrentPriceResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: CurrentPriceData?
)

data class CurrentPriceData(
    @SerializedName("price_per_kg")
    val pricePerKg: Double,

    @SerializedName("grade")
    val grade: String,

    @SerializedName("effective_from")
    val effectiveFrom: String,

    @SerializedName("currency")
    val currency: String,

    @SerializedName("next_review_date")
    val nextReviewDate: String?,

    @SerializedName("price_change")
    val priceChange: PriceChangeInfo?
)

data class PriceChangeInfo(
    @SerializedName("previous_price")
    val previousPrice: Double,

    @SerializedName("change_amount")
    val changeAmount: Double,

    @SerializedName("change_percentage")
    val changePercentage: Double,

    @SerializedName("change_direction")
    val changeDirection: String // "INCREASE", "DECREASE", "NO_CHANGE"
)