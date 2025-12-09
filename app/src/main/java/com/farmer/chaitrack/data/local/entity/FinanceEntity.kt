package com.farmer.chaitrack.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Index

@Entity(
    tableName = "finance_records",
    indices = [
        Index(value = ["year", "month"], name = "index_finance_year_month", unique = true),
        Index(value = ["userId"], name = "index_finance_userId"),
        Index(value = ["year"], name = "index_finance_year")
    ]
)
data class FinanceEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "userId")
    val userId: String,

    @ColumnInfo(name = "year")
    val year: Int, // e.g., 2024

    @ColumnInfo(name = "month")
    val month: Int,

    @ColumnInfo(name = "totalWeight")
    val totalWeight: Double,

    @ColumnInfo(name = "pricePerKg")
    val pricePerKg: Double,

    @ColumnInfo(name = "totalEarnings")
    val totalEarnings: Double,

    @ColumnInfo(name = "deliveryCount")
    val deliveryCount: Int = 0,

    @ColumnInfo(name = "averageDeliveryWeight")
    val averageDeliveryWeight: Double = 0.0,

    @ColumnInfo(name = "pdfGenerated")
    val pdfGenerated: Boolean = false,

    @ColumnInfo(name = "pdfFilePath")
    val pdfFilePath: String? = null,

    @ColumnInfo(name = "pdfGeneratedAt")
    val pdfGeneratedAt: Long? = null,

    @ColumnInfo(name = "pdfShared")
    val pdfShared: Boolean = false,

    @ColumnInfo(name = "pdfSharedAt")
    val pdfSharedAt: Long? = null,

    @ColumnInfo(name = "paymentStatus")
    val paymentStatus: String = "PENDING",

    @ColumnInfo(name = "paymentDate")
    val paymentDate: Long? = null,

    @ColumnInfo(name = "paymentReference")
    val paymentReference: String? = null,

    @ColumnInfo(name = "bankAccount")
    val bankAccount: String? = null,

    @ColumnInfo(name = "deductions")
    val deductions: Double = 0.0,

    @ColumnInfo(name = "deductionReason")
    val deductionReason: String? = null,

    @ColumnInfo(name = "netEarnings")
    val netEarnings: Double,

    @ColumnInfo(name = "notes")
    val notes: String? = null,

    @ColumnInfo(name = "createdAt")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "updatedAt")
    val updatedAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "syncedToServer")
    val syncedToServer: Boolean = false,

    @ColumnInfo(name = "serverId")
    val serverId: String? = null
)