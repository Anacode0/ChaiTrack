package com.farmer.chaitrack.data.remote.dto

import com.farmer.chaitrack.data.local.entity.FinanceEntity
import com.farmer.chaitrack.data.local.preferences.PreferencesManager
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

// DTO class definition - data from API
data class FinanceRecordDto(
    @SerializedName("id")
    val id: String?,
    @SerializedName("year")
    val year: Int?,
    @SerializedName("month")
    val month: Int?,
    val date: Long,
    val weight: Double,
    val amount: Double,
    val type: String,
    @SerializedName("period")
    val period: String, // "January 2024"
    @SerializedName("total_weight")
    val totalWeight: Double?,
    @SerializedName("price_per_kg")
    val pricePerKg: Double?,
    @SerializedName("total_earnings")
    val totalEarnings: Double?,
    @SerializedName("delivery_count")
    val deliveryCount: Int?,
    @SerializedName("average_delivery_weight")
    val averageDeliveryWeight: Double?,
    @SerializedName("pdf_generated")
    val pdfGenerated: Boolean?,
    @SerializedName("pdf_file_path")
    val pdfFilePath: String?,
    @SerializedName("pdf_generated_at")
    val pdfGeneratedAt: String?,
    @SerializedName("pdf_shared")
    val pdfShared: Boolean?,
    @SerializedName("pdf-shared_at")
    val pdfSharedAt: String?,
    @SerializedName("payment_status")
    val paymentStatus: String?,
    @SerializedName("payment_date")
    val paymentDate: String?,
    @SerializedName("payment_reference")
    val paymentReference: String?,
    @SerializedName("bank_account")
    val bankAccount: String?,
    @SerializedName("deductions")
    val deductions: Double?,
    @SerializedName("deduction_reason")
    val deductionReason: String?,
    @SerializedName("net_earnings")
    val netEarnings: Double?,
    @SerializedName("notes")
    val notes: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("updated_at")
    val updatedAt: String?
)

// Helper function to parse date strings to timestamps (API 24 compatible)
private fun parseDateToTimestamp(dateString: String?): Long {
    if (dateString.isNullOrBlank()) return 0L

    return try {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        format.timeZone = TimeZone.getDefault()
        format.parse(dateString)?.time ?: 0L
    } catch (e: Exception) {
        0L
    }
}

// See what properties are available
fun debugFinanceRecordProperties(record: FinanceRecordDto) {
    println("Available properties:")
    println("year: ${record.year}")
    println("month: ${record.month}")
    println("totalWeight: ${record.totalWeight}")
    println("pricePerKg: ${record.pricePerKg}")
    println("totalEarnings: ${record.totalEarnings}")
    println("deliveryCount: ${record.deliveryCount}")
    println("paymentStatus: ${record.paymentStatus}")
    println("deductions: ${record.deductions}")
    println("netEarnings: ${record.netEarnings}")
    // Add other properties you need to check
}

// Extension function to convert DTO to Entity
fun FinanceRecordDto.toEntity(preferencesManager: PreferencesManager): FinanceEntity {
    val userId = preferencesManager.getUserId() ?: ""
    val currentTime = System.currentTimeMillis()

    return FinanceEntity(
        id = this.id ?: "",
        userId = userId,
        year = this.year ?: 0,
        month = this.month ?: 0,
        totalWeight = this.totalWeight ?: 0.0,
        pricePerKg = this.pricePerKg ?: 0.0,
        totalEarnings = this.totalEarnings ?: 0.0,
        deliveryCount = this.deliveryCount ?: 0,
        averageDeliveryWeight = this.averageDeliveryWeight ?: 0.0,
        pdfGenerated = this.pdfGenerated ?: false,
        pdfFilePath = this.pdfFilePath ?: "", // Fixed elvis operator warning
        pdfGeneratedAt = parseDateToTimestamp(this.pdfGeneratedAt),
        pdfShared = this.pdfShared ?: false,
        pdfSharedAt = parseDateToTimestamp(this.pdfSharedAt),
        paymentStatus = this.paymentStatus ?: "PENDING",
        paymentDate = parseDateToTimestamp(this.paymentDate),
        paymentReference = this.paymentReference ?: "", // Fixed elvis operator warning
        bankAccount = this.bankAccount ?: "", // Fixed elvis operator warning
        deductions = this.deductions ?: 0.0,
        deductionReason = this.deductionReason ?: "", // Fixed elvis operator warning
        netEarnings = this.netEarnings ?: (this.totalEarnings ?: 0.0),
        notes = this.notes ?: "", // Fixed elvis operator warning
        createdAt = parseDateToTimestamp(this.createdAt),
        updatedAt = parseDateToTimestamp(this.updatedAt),
        syncedToServer = true,
        serverId = this.id
    )
}