package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName


data class DeliveryDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("date")
    val date: Long, // yyyy-MM-dd

    val userId: String,

    val monthTotal: Double? = null,

    @SerializedName("time")
    val time: String, // HH:mm:ss

    @SerializedName("weight")
    val weight: Double, // in kg

    @SerializedName("clerk_name")
    val clerkName: String,

    @SerializedName("collection_center")
    val collectionCenter: String,

    @SerializedName("price_per_kg")
    val pricePerKg: Double,

    @SerializedName("total_amount")
    val totalAmount: Double,

    @SerializedName("status")
    val status: String, // "PENDING", "PROCESSED", "REJECTED", "PAID"

    @SerializedName("docket_number")
    val docketNumber: String?,

    @SerializedName("grade")
    val grade: String?,

    @SerializedName("notes")
    val notes: String?,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String?
)


// Response wrapper for deliveries
data class DeliveryResponse(
    val success: Boolean,
    val message: String? = null,
    val data: DeliveryData
)

data class DeliveryData(
    val deliveries: List<DeliveryDto>
)



data class DeliveryStatisticsData(
    @SerializedName("period")
    val period: PeriodInfo,

    @SerializedName("overall")
    val overall: DeliveryStats,

    @SerializedName("monthly_breakdown")
    val monthlyBreakdown: List<MonthlyStats>,

    @SerializedName("comparison")
    val comparison: ComparisonStats?
)