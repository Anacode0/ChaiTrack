package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ApplicationTimelineResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: ApplicationTimelineData?
)

data class ApplicationTimelineData(
    @SerializedName("timeline")
    val timeline: List<TimelineEvent>,

    @SerializedName("current_stage")
    val currentStage: String,

    @SerializedName("estimated_completion")
    val estimatedCompletion: String?,

    @SerializedName("next_milestone")
    val nextMilestone: TimelineEvent?
)

data class TimelineEvent(
    @SerializedName("stage")
    val stage: String,

    @SerializedName("status")
    val status: String, // "COMPLETED", "IN_PROGRESS", "PENDING"

    @SerializedName("date")
    val date: String?,

    @SerializedName("timestamp")
    val timestamp: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("actor")
    val actor: String?,

    @SerializedName("note")
    val notes: String?
)