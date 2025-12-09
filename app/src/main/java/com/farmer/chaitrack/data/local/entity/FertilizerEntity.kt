package com.farmer.chaitrack.data.local.entity

import androidx.room.*
import com.farmer.chaitrack.domain.model.FertilizerApplication

@Entity( tableName = "fertilizer_applications",
    indices = [
        Index(value = ["season"], name = "index_fertilizer_season"),
        Index(value = ["status"], name = "index_fertilizer_status"),
        Index(value = ["userId"], name = "index_fertilizer_userId"),
        Index(value = ["createdAt"], name = "index_fertilizer_createdAt")
    ]
)
data class FertilizerEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "userId")
    val userId: String,

    @ColumnInfo(name = "season")
    val season: String,

    @ColumnInfo(name = "plotSize")
    val plotSize: Double,

    @ColumnInfo(name = "quantityRequested")
    val quantityRequested: Int,

    @ColumnInfo(name = "quantityApproved")
    val quantityApproved: Int = 0,

    @ColumnInfo(name = "recommendedQuantity")
    val recommendedQuantity: Int,

    @ColumnInfo(name = "status")
    val status: String,

    @ColumnInfo(name = "applicationDate")
    val applicationDate: Long,

    @ColumnInfo(name = "approvalDate")
    val approvalDate: Long? = null,

    @ColumnInfo(name = "pickupDate")
    val pickupDate: Long? = null,

    @ColumnInfo(name = "completionDate")
    val completionDate: Long? = null,

    @ColumnInfo(name = "approvedBy")
    val approvedBy: String? = null,

    @ColumnInfo(name = "approverId")
    val approverId: String? = null,

    @ColumnInfo(name = "rejectionReason")
    val rejectionReason: String? = null,

    @ColumnInfo(name = "pickupLocation")
    val pickupLocation: String? = null,

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