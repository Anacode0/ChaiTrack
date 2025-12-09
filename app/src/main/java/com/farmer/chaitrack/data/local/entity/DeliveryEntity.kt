package com.farmer.chaitrack.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Index

@Entity(
    tableName = "deliveries",
    indices = [
        Index(value = ["date"], name = "index_deliveries_date"),
        Index(value = ["userId"], name = "index_deliveries_userId")
    ]
)
data class DeliveryEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "userId")
    val userId: String,

    @ColumnInfo(name = "date")
    val date: Long,

    @ColumnInfo(name = "time")
    val time: String,

    @ColumnInfo(name = "weight")
    val weight: Double,

    @ColumnInfo(name = "clerkName")
    val clerkName: String,

    val pricePerKg: Double? = null,

    @ColumnInfo(name = "clerkId")
    val clerkId: String? = null,

    @ColumnInfo(name = "monthTotal")
    val monthTotal: Double? = null,

    val totalAmount: Double? = null,

    @ColumnInfo(name = "receiptNumber")
    val receiptNumber: String? = null,

    @ColumnInfo(name = "collectionCenter")
    val collectionCenter: String? = null,

    @ColumnInfo(name = "notes")
    val notes: String? = null,

    @ColumnInfo(name = "status")
    val status: String = "CONFIRMED",

    @ColumnInfo(name = "createdAt")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "updatedAt")
    val updatedAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "syncedToServer")
    val syncedToServer: Boolean = false,

    @ColumnInfo(name = "serverId")
    val serverId: String? = null
)