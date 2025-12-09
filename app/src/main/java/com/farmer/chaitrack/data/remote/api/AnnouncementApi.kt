package com.farmer.chaitrack.data.remote.api

import com.farmer.chaitrack.data.remote.dto.Announcement
import retrofit2.Response
import retrofit2.http.*

interface AnnouncementApi {

    @GET("announcements")
    suspend fun getAnnouncements(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Response<List<Announcement>>

    @GET("announcements/category/{category}")
    suspend fun getAnnouncementsByCategory(
        @Header("Authorization") token: String,
        @Path("category") category: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Response<List<Announcement>>

    @GET("announcements/{id}")
    suspend fun getAnnouncementById(
        @Header("Authorization") token: String,
        @Path("id") announcementId: String
    ): Response<Announcement>

    @GET("announcements/unread/count")
    suspend fun getUnreadAnnouncementsCount(
        @Header("Authorization") token: String
    ): Response<Int>

    @POST("announcements/{id}/read")
    suspend fun markAsRead(
        @Header("Authorization") token: String,
        @Path("id") announcementId: String
    ): Response<Unit>

    @POST("announcements/mark-all-read")
    suspend fun markAllAsRead(
        @Header("Authorization") token: String
    ): Response<Unit>

    @GET("announcements/urgent")
    suspend fun getUrgentAnnouncements(
        @Header("Authorization") token: String,
        @Query("limit") limit: Int = 5
    ): Response<List<Announcement>>

    @GET("announcements/search")
    suspend fun searchAnnouncements(
        @Header("Authorization") token: String,
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Response<List<Announcement>>

    @GET("announcements/since")
    suspend fun getAnnouncementsSince(
        @Header("Authorization") token: String,
        @Query("since") since: String, // ISO date format
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 50
    ): Response<List<Announcement>>
}