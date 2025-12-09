package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PaymentHistoryResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: PaymentHistoryData?
)

data class PaymentHistoryData(
    @SerializedName("payments")
    val payments: List<PaymentDto>,

    @SerializedName("pagination")
    val pagination: PaginationInfo,

    @SerializedName("summary")
    val summary: PaymentSummary
)

data class PaymentDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("period")
    val period: String,

    @SerializedName("amount")
    val amount: Double,

    @SerializedName("payment_date")
    val paymentDate: String,

    @SerializedName("payment_method")
    val paymentMethod: String,

    @SerializedName("status")
    val status: String, // "COMPLETED", "PENDING", "FAILED"

    @SerializedName("transaction_id")
    val transactionId: String?,

    @SerializedName("reference_number")
    val referenceNumber: String?
)

data class PaymentSummary(
    @SerializedName("total_payments")
    val totalPayments: Double,

    @SerializedName("completed_payments")
    val completedPayments: Int,

    @SerializedName("pending_payments")
    val pendingPayments: Int
)