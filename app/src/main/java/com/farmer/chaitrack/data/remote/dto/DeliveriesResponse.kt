package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class DeliveriesResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: DeliveriesData?
)

data class DeliveriesData(
    @SerializedName("deliveries")
    val deliveries: List<DeliveryDto>,

    @SerializedName("pagination")
    val pagination: PaginationInfo,

    @SerializedName("summary")
    val summary: DeliverySummary?
)

data class PaginationInfo(
    @SerializedName("current_page")
    val currentPage: Int,

    @SerializedName("total_pages")
    val totalPages: Int,

    @SerializedName("total_items")
    val totalItems: Int,

    @SerializedName("has_next")
    val hasNext: Boolean,

    @SerializedName("has_previous")
    val hasPrevious: Boolean
)

data class DeliverySummary(
    @SerializedName("total_weight")
    val totalWeight: Double,

    @SerializedName("total_amount")
    val totalAmount: Double,

    @SerializedName("delivery_count")
    val deliveryCount: Int,

    @SerializedName("average_weight")
    val averageWeight: Double
)