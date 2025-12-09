package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TranscriptHistoryResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: TranscriptHistoryData?
)

data class TranscriptHistoryData(
    @SerializedName("transcripts")
    val transcripts: List<TranscriptHistoryItem>,

    @SerializedName("pagination")
    val pagination: PaginationInfo
)

data class TranscriptHistoryItem(
    @SerializedName("id")
    val id: String,

    @SerializedName("period")
    val period: String,

    @SerializedName("purpose")
    val purpose: String,

    @SerializedName("generated_at")
    val generatedAt: String,

    @SerializedName("file_size")
    val fileSize: Long,

    @SerializedName("download_count")
    val downloadCount: Int,

    @SerializedName("share_count")
    val shareCount: Int
)