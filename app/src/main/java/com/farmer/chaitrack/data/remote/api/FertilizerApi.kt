package com.farmer.chaitrack.data.remote.api

import com.farmer.chaitrack.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface FertilizerApi {

    @GET("fertilizer/applications")
    suspend fun getApplications(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 50
    ): Response<FertilizerApplicationsResponse>

    @GET("fertilizer/applications/{id}")
    suspend fun getApplicationById(
        @Header("Authorization") token: String,
        @Path("id") applicationId: String
    ): Response<FertilizerApplicationDetailResponse>

    @GET("fertilizer/applications/current")
    suspend fun getCurrentApplication(
        @Header("Authorization") token: String
    ): Response<FertilizerApplicationDetailResponse>

    @GET("fertilizer/applications/history")
    suspend fun getApplicationHistory(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Response<FertilizerApplicationsResponse>

    /** @GET("fertilizer/applications/status")
    suspend fun getApplicationsByStatus(
        @Header("Authorization") token: String,
        @Query("status") status: String, // PENDING, APPROVED, READY_FOR_PICKUP, REJECTED, COMPLETED
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 50
    ): Response<FertilizerApplicationsResponse>
    */

    /** @GET("fertilizer/applications/season")
    suspend fun getApplicationsBySeason(
        @Header("Authorization") token: String,
        @Query("season") season: String, // e.g., "Spring 2024"
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 50
    ): Response<FertilizerApplicationsResponse>
    */

    @GET("fertilizer/applications/year")
    suspend fun getApplicationsByYear(
        @Header("Authorization") token: String,
        @Query("year") year: Int,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 50
    ): Response<FertilizerApplicationsResponse>

    @POST("fertilizer/applications")
    suspend fun submitApplication(
        @Header("Authorization") token: String,
        @Body request: SubmitFertilizerApplicationRequest
    ): Response<SubmitFertilizerApplicationResponse>

    /** @PUT("fertilizer/applications/{id}")
    suspend fun updateApplication(
        @Header("Authorization") token: String,
        @Path("id") applicationId: String,
        @Body request: UpdateFertilizerApplicationRequest
    ): Response<UpdateFertilizerApplicationResponse>
    */

    @DELETE("fertilizer/applications/{id}")
    suspend fun cancelApplication(
        @Header("Authorization") token: String,
        @Path("id") applicationId: String
    ): Response<CancelFertilizerApplicationResponse>

    @GET("fertilizer/config")
    suspend fun getApplicationConfig(
        @Header("Authorization") token: String
    ): Response<FertilizerConfigResponse>

    @GET("fertilizer/applications/status/open")
    suspend fun areApplicationsOpen(
        @Header("Authorization") token: String
    ): Response<ApplicationsOpenResponse>

    @GET("fertilizer/seasons")
    suspend fun getAvailableSeasons(
        @Header("Authorization") token: String
    ): Response<AvailableSeasonsResponse>

    @GET("fertilizer/recommended-quantity")
    suspend fun getRecommendedQuantity(
        @Header("Authorization") token: String,
        @Query("plotSize") plotSize: Double
    ): Response<RecommendedQuantityResponse>

    /** @GET("fertilizer/pickup-locations")
    suspend fun getPickupLocations(
        @Header("Authorization") token: String
    ): Response<PickupLocationsResponse>
    */

    @POST("fertilizer/applications/{id}/confirm-pickup")
    suspend fun confirmPickup(
        @Header("Authorization") token: String,
        @Path("id") applicationId: String,
        @Body request: ConfirmPickupRequest
    ): Response<ConfirmPickupResponse>

    @GET("fertilizer/statistics")
    suspend fun getApplicationStatistics(
        @Header("Authorization") token: String
    ): Response<FertilizerStatisticsResponse>

    @GET("fertilizer/applications/{id}/timeline")
    suspend fun getApplicationTimeline(
        @Header("Authorization") token: String,
        @Path("id") applicationId: String
    ): Response<ApplicationTimelineResponse>

    @POST("fertilizer/applications/{id}/notify")
    suspend fun requestStatusNotification(
        @Header("Authorization") token: String,
        @Path("id") applicationId: String
    ): Response<NotificationRequestResponse>

    @POST("fertilizer/applications/{id}/report-issue")
    suspend fun reportApplicationIssue(
        @Header("Authorization") token: String,
        @Path("id") applicationId: String,
        @Body request: ReportIssueRequest
    ): Response<ReportIssueResponse>

    /** @GET("fertilizer/guidelines")
    suspend fun getFertilizerGuidelines(
        @Header("Authorization") token: String
    ): Response<FertilizerGuidelinesResponse>
    */

    @GET("fertilizer/applications/export/pdf")
    @Streaming
    suspend fun exportApplicationsToPdf(
        @Header("Authorization") token: String,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null
    ): Response<okhttp3.ResponseBody>

    @GET("fertilizer/applications/{id}/receipt")
    @Streaming
    suspend fun downloadApplicationReceipt(
        @Header("Authorization") token: String,
        @Path("id") applicationId: String
    ): Response<okhttp3.ResponseBody>
}