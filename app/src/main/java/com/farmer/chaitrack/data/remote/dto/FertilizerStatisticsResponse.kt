package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class FertilizerStatisticsResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: FertilizerStatisticsData?
)

data class FertilizerStatisticsData(
    @SerializedName("overall_stats")
    val overallStats: OverallFertilizerStats,

    @SerializedName("seasonal_breakdown")
    val seasonalBreakdown: List<SeasonalStats>,

    @SerializedName("approval_rates")
    val approvalRates: ApprovalRateStats
)

data class OverallFertilizerStats(
    @SerializedName("total_applications")
    val totalApplications: Int,

    @SerializedName("approval_rate")
    val approvalRate: Double,

    @SerializedName("total_fertilizer_received")
    val totalFertilizerReceived: Int, // in bags

    @SerializedName("average_plot_size")
    val averagePlotSize: Double,

    @SerializedName("successful_seasons")
    val successfulSeasons: Int
)

data class SeasonalStats(
    @SerializedName("season")
    val season: String,

    @SerializedName("applications_submitted")
    val applicationsSubmitted: Int,

    @SerializedName("applications_approved")
    val applicationsApproved: Int,

    @SerializedName("fertilizer_received")
    val fertilizerReceived: Int,

    @SerializedName("approval_rate")
    val approvalRate: Double
)

data class ApprovalRateStats(
    @SerializedName("pending")
    val pending: Int,

    @SerializedName("approved")
    val approved: Int,

    @SerializedName("rejected")
    val rejected: Int,

    @SerializedName("ready_for_pickup")
    val readyForPickup: Int,

    @SerializedName("completed")
    val completed: Int
)