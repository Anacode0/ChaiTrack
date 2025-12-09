package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GenerateTranscriptRequest(
    @SerializedName("year")
    val year: Int,

    @SerializedName("month")
    val month: Int,

    @SerializedName("purpose")
    val purpose: String? = null, // "BANK_LOAN", "PERSONAL_RECORD", "OTHER"

    @SerializedName("include_detailed_breakdown")
    val includeDetailedBreakdown: Boolean = false,

    @SerializedName("language")
    val language: String = "en" // "en", "sw"
)