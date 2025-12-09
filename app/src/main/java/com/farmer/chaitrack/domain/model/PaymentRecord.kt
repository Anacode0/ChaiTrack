package com.farmer.chaitrack.domain.model

import java.time.LocalDate

data class PaymentRecord(
    val id: String,
    val transactionId: String,
    val amount: Double,
    val paymentDate: LocalDate,
    val paymentMethod: PaymentMethod,
    val status: PaymentStatus,
    val description: String,
    val farmerId: String,
    val receiptNumber: String? = null,
    val bankReference: String? = null,
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    fun isCompleted(): Boolean = status == PaymentStatus.COMPLETED

    fun isPending(): Boolean = status == PaymentStatus.PENDING

    fun isFailed(): Boolean = status == PaymentStatus.FAILED
}

enum class PaymentMethod {
    BANK_TRANSFER,
    CASH,
    MOBILE_MONEY,
    CHEQUE,
    OTHER
}

enum class PaymentStatus {
    PENDING,
    COMPLETED,
    FAILED,
    CANCELLED,
    REFUNDED
}