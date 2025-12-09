package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO for price API responses
 */
data class PriceResponseDto(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("data")
    val data: PriceData? = null,

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
     * Get price value safely
     */
    fun getPrice(): Double? {
        return data?.currentPrice
    }
}
/**
 * Individual price history item
 */
data class PriceHistoryItemDto(
    @SerializedName("date")
    val date: String, // ISO date format: "2024-01-15"

    @SerializedName("price")
    val price: Double,

    @SerializedName("priceType")
    val priceType: String? = "CURRENT", // CURRENT, HISTORICAL, PROJECTED

    @SerializedName("grade")
    val grade: String? = null,

    @SerializedName("collectionCenter")
    val collectionCenter: String? = null,

    @SerializedName("effectiveFrom")
    val effectiveFrom: String? = null,

    @SerializedName("effectiveTo")
    val effectiveTo: String? = null,

    @SerializedName("notes")
    val notes: String? = null
)

/**
 * Price trend information
 */
data class PriceTrend(
    @SerializedName("direction")
    val direction: String, // UP, DOWN, STABLE

    @SerializedName("percentageChange")
    val percentageChange: Double? = null,

    @SerializedName("changeAmount")
    val changeAmount: Double? = null,

    @SerializedName("period")
    val period: String? = null, // e.g., "WEEK", "MONTH", "QUARTER"

    @SerializedName("confidence")
    val confidence: Double? = null // 0.0 to 1.0
)

/**
 * Request DTO for price updates
 */
data class UpdatePriceRequestDto(
    @SerializedName("price")
    val price: Double,

    @SerializedName("effectiveFrom")
    val effectiveFrom: String, // ISO date format

    @SerializedName("grade")
    val grade: String? = null,

    @SerializedName("collectionCenter")
    val collectionCenter: String? = null,

    @SerializedName("notes")
    val notes: String? = null
)

/**
 * Request DTO for price history
 */
data class PriceHistoryRequestDto(
    @SerializedName("startDate")
    val startDate: String, // ISO date format

    @SerializedName("endDate")
    val endDate: String, // ISO date format

    @SerializedName("grade")
    val grade: String? = null,

    @SerializedName("collectionCenter")
    val collectionCenter: String? = null,

    @SerializedName("priceType")
    val priceType: String? = null
)

/**
 * Bulk price update request
 */
data class BulkPriceUpdateRequestDto(
    @SerializedName("prices")
    val prices: List<BulkPriceItemDto>,

    @SerializedName("effectiveFrom")
    val effectiveFrom: String
)

/**
 * Individual item for bulk price update
 */
data class BulkPriceItemDto(
    @SerializedName("grade")
    val grade: String,

    @SerializedName("price")
    val price: Double,

    @SerializedName("collectionCenter")
    val collectionCenter: String? = null
)

/**
 * Price comparison DTO
 */
data class PriceComparisonDto(
    @SerializedName("currentPrice")
    val currentPrice: Double,

    @SerializedName("previousPrice")
    val previousPrice: Double,

    @SerializedName("changeAmount")
    val changeAmount: Double,

    @SerializedName("percentageChange")
    val percentageChange: Double,

    @SerializedName("changeDirection")
    val changeDirection: String, // UP, DOWN, STABLE

    @SerializedName("period")
    val period: String,

    @SerializedName("comparisonDate")
    val comparisonDate: String
)

/**
 * Seasonal price data
 */
data class SeasonalPriceDto(
    @SerializedName("season")
    val season: String, // e.g., "SPRING", "SUMMER", "AUTUMN", "WINTER"

    @SerializedName("year")
    val year: Int,

    @SerializedName("averagePrice")
    val averagePrice: Double,

    @SerializedName("minPrice")
    val minPrice: Double,

    @SerializedName("maxPrice")
    val maxPrice: Double,

    @SerializedName("priceVolatility")
    val priceVolatility: Double? = null,

    @SerializedName("transactionCount")
    val transactionCount: Int? = null
)