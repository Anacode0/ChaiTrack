package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GenerateTranscriptResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: GenerateTranscriptData?
)

data class GenerateTranscriptData(
    @SerializedName("transcript_id")
    val transcriptId: String,

    @SerializedName("download_url")
    val downloadUrl: String,

    @SerializedName("expires_at")
    val expiresAt: String,

    @SerializedName("file_size")
    val fileSize: Long,

    val pdfUrl: String?,

    @SerializedName("verification_code")
    val verificationCode: String?
)