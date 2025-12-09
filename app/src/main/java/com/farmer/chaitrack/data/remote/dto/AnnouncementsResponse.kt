package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AnnouncementsResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: AnnouncementsData?
)

data class AnnouncementsData(
    @SerializedName("announcements")
    val announcements: List<Announcement>
)

data class AnnouncementDetailResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: AnnouncementDetailData?
)

data class AnnouncementDetailData(
    @SerializedName("announcement")
    val announcement: Announcement
)