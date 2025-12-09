package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PaymentDetailResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: PaymentDetailData?
)

data class PaymentDetailData(
    @SerializedName("payment")
    val payment: PaymentDto,

    @SerializedName("finance_records")
    val financeRecords: List<FinanceRecordDto>,

    @SerializedName("bank_details")
    val bankDetails: BankDetails?,

    @SerializedName("payment_breakdown")
    val paymentBreakdown: PaymentBreakdown
)

data class BankDetails(
    @SerializedName("bank_name")
    val bankName: String,

    @SerializedName("account_number")
    val accountNumber: String,

    @SerializedName("account_name")
    val accountName: String,

    @SerializedName("branch")
    val branch: String?
)

data class PaymentBreakdown(
    @SerializedName("basic_earnings")
    val basicEarnings: Double,

    @SerializedName("bonuses")
    val bonuses: Double?,

    @SerializedName("deductions")
    val deductions: Double,

    @SerializedName("tax")
    val tax: Double?,

    @SerializedName("net_amount")
    val netAmount: Double
)