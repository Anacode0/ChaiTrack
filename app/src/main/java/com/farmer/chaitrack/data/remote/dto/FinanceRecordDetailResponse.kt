package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class FinanceRecordDetailResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: FinanceRecordDetailData?
)

data class FinanceRecordDetailData(
    @SerializedName("record")
    val record: FinanceRecordDto,

    @SerializedName("delivery_breakdown")
    val deliveryBreakdown: List<DeliveryBreakdown>,

    @SerializedName("deduction_details")
    val deductionDetails: List<DeductionDetail>,

    @SerializedName("payment_details")
    val paymentDetails: PaymentDetails?
)

data class DeductionDetail(
    @SerializedName("type")
    val type: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("amount")
    val amount: Double,

    @SerializedName("rate")
    val rate: Double?
)

data class PaymentDetails(
    @SerializedName("payment_method")
    val paymentMethod: String,

    @SerializedName("transaction_id")
    val transactionId: String?,

    @SerializedName("bank_name")
    val bankName: String?,

    @SerializedName("account_number")
    val accountNumber: String?,

    @SerializedName("payment_reference")
    val paymentReference: String?
)