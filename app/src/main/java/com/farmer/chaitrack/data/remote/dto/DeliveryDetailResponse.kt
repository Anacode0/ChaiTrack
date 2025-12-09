package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class DeliveryDetailResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: DeliveryDetailData?
)

data class DeliveryDetailData(
    @SerializedName("delivery")
    val delivery: DeliveryDto,

    @SerializedName("related_deliveries")
    val relatedDeliveries: List<DeliveryDto>?,

    @SerializedName("quality_metrics")
    val qualityMetrics: QualityMetrics?
)

data class QualityMetrics(
    @SerializedName("moisture_content")
    val moistureContent: Double?,

    @SerializedName("leaf_quality")
    val leafQuality: String?,

    @SerializedName("impurities")
    val impurities: Double?,

    @SerializedName("remarks")
    val remarks: String?
)