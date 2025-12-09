package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RecommendedQuantityResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: RecommendedQuantityData?
)

data class RecommendedQuantityData(
    @SerializedName("plot_size")
    val plotSize: Double,

    @SerializedName("recommended_quantity")
    val recommendedQuantity: Int,

    @SerializedName("calculation_details")
    val calculationDetails: CalculationDetails,

    @SerializedName("allowed_range")
    val allowedRange: QuantityRange,

    @SerializedName("fertilizer_type")
    val fertilizerType: String
)

data class CalculationDetails(
    @SerializedName("rate_per_acre")
    val ratePerAcre: Double,

    @SerializedName("calculated_quantity")
    val calculatedQuantity: Double,

    @SerializedName("rounded_quantity")
    val roundedQuantity: Int,

    @SerializedName("formula_used")
    val formulaUsed: String
)

data class QuantityRange(
    @SerializedName("min")
    val min: Int,

    @SerializedName("max")
    val max: Int,

    @SerializedName("default")
    val default: Int
)