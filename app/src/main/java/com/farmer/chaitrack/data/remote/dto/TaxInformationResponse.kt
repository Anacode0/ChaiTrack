package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TaxInformationResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: TaxInformationData?
)

data class TaxInformationData(
    @SerializedName("year")
    val year: Int,

    @SerializedName("tax_year")
    val taxYear: String,

    @SerializedName("total_earnings")
    val totalEarnings: Double,

    @SerializedName("taxable_income")
    val taxableIncome: Double,

    @SerializedName("tax_paid")
    val taxPaid: Double,

    @SerializedName("tax_breakdown")
    val taxBreakdown: List<TaxBreakdown>,

    @SerializedName("tax_certificate_url")
    val taxCertificateUrl: String?,

    @SerializedName("filing_status")
    val filingStatus: String // "FILED", "PENDING", "NOT_REQUIRED"
)

data class TaxBreakdown(
    @SerializedName("tax_type")
    val taxType: String,

    @SerializedName("amount")
    val amount: Double,

    @SerializedName("rate")
    val rate: Double,

    @SerializedName("description")
    val description: String
)