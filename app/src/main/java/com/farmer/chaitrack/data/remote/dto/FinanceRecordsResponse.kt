package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class FinanceRecordsResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: FinanceRecordsData?
)

data class FinanceRecordsData(
    @SerializedName("records")
    val records: List<FinanceRecordDto>,

    @SerializedName("pagination")
    val pagination: PaginationInfo,

    @SerializedName("summary")
    val summary: FinanceRecordsSummary
)
data class FinanceRecordsSummary(
    @SerializedName("total_earnings")
    val totalEarnings: Double,

    @SerializedName("total_deductions")
    val totalDeductions: Double,

    @SerializedName("net_earnings")
    val netEarnings: Double,

    @SerializedName("record_count")
    val recordCount: Int
)