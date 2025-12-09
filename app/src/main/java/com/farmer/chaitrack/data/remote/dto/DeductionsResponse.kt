package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class DeductionsResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: DeductionsData?
)

data class DeductionsData(
    @SerializedName("year")
    val year: Int,

    @SerializedName("month")
    val month: Int,

    @SerializedName("total_deductions")
    val totalDeductions: Double,

    @SerializedName("deduction_categories")
    val deductionCategories: List<DeductionCategory>,

    @SerializedName("monthly_trend")
    val monthlyTrend: List<MonthlyDeduction>
)

data class DeductionCategory(
    @SerializedName("category")
    val category: String,

    @SerializedName("amount")
    val amount: Double,

    @SerializedName("percentage")
    val percentage: Double,

    @SerializedName("description")
    val description: String
)

data class MonthlyDeduction(
    @SerializedName("month")
    val month: String,

    @SerializedName("amount")
    val amount: Double,

    @SerializedName("percentage_of_earnings")
    val percentageOfEarnings: Double
)