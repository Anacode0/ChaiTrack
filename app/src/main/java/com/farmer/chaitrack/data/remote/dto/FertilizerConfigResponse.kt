package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class FertilizerConfigResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: FertilizerConfigData?
)

data class FertilizerConfigData(
    @SerializedName("application_period")
    val applicationPeriod: ApplicationPeriod,

    @SerializedName("available_fertilizers")
    val availableFertilizers: List<FertilizerType>,

    @SerializedName("calculation_formula")
    val calculationFormula: CalculationFormula,

    @SerializedName("max_plot_size")
    val maxPlotSize: Double,

    @SerializedName("min_plot_size")
    val minPlotSize: Double,

    @SerializedName("max_quantity_per_application")
    val maxQuantityPerApplication: Int,

    @SerializedName("application_fee")
    val applicationFee: Double?,

    @SerializedName("application_window_open")
    val applicationWindowOpen: Boolean,

    @SerializedName("terms_and_conditions")
    val termsAndConditions: String
)

data class ApplicationPeriod(
    @SerializedName("start_date")
    val startDate: String,

    @SerializedName("end_date")
    val endDate: String,

    @SerializedName("is_currently_open")
    val isCurrentlyOpen: Boolean,

    @SerializedName("days_remaining")
    val daysRemaining: Int
)

data class FertilizerType(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("recommended_rate")
    val recommendedRate: Double, // kg per acre

    @SerializedName("price_per_bag")
    val pricePerBag: Double?,

    @SerializedName("available_quantity")
    val availableQuantity: Int
)

data class CalculationFormula(
    @SerializedName("rate_per_acre")
    val ratePerAcre: Double,

    @SerializedName("bag_size_kg")
    val bagSizeKg: Double,

    @SerializedName("rounding_method")
    val roundingMethod: String // "CEILING", "FLOOR", "ROUND"
)