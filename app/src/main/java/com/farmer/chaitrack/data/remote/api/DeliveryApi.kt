package com.farmer.chaitrack.data.remote.api

import com.farmer.chaitrack.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface DeliveryApi {
    @POST("deliveries/{deliveryId}/report-issue")
    suspend fun reportDeliveryIssue(
        @Header("Authorization") token: String,
        @Body request: ReportIssueRequest
    ): Response<ApiResponse<Unit>>

    @GET("deliveries")
    suspend fun getDeliveries(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 50
    ): Response<DeliveriesResponse>

    @GET("deliveries/{id}")
    suspend fun getDeliveryById(
        @Header("Authorization") token: String,
        @Path("id") deliveryId: String
    ): Response<DeliveryDetailResponse>

    @GET("deliveries/range")
    suspend fun getDeliveriesByDateRange(
        @Header("Authorization") token: String,
        @Query("startDate") startDate: String, // Format: yyyy-MM-dd
        @Query("endDate") endDate: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 50
    ): Response<DeliveriesResponse>

    @GET("deliveries/month")
    suspend fun getDeliveriesByMonth(
        @Header("Authorization") token: String,
        @Query("year") year: Int,
        @Query("month") month: Int, // 1-12
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 50
    ): Response<DeliveriesResponse>

    @GET("deliveries/year")
    suspend fun getDeliveriesByYear(
        @Header("Authorization") token: String,
        @Query("year") year: Int,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 50
    ): Response<DeliveriesResponse>

    @GET("deliveries/statistics")
    suspend fun getDeliveryStatistics(
        @Header("Authorization") token: String,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null
    ): Response<DeliveryStatisticsResponse>

    @GET("deliveries/summary/month")
    suspend fun getMonthlyDeliverySummary(
        @Header("Authorization") token: String,
        @Query("year") year: Int,
        @Query("month") month: Int
    ): Response<MonthlyDeliverySummaryResponse>

    @GET("deliveries/today")
    suspend fun getTodayDeliveries(
        @Header("Authorization") token: String
    ): Response<DeliveriesResponse>

    /**@POST("deliveries")
    suspend fun createDelivery(
        @Header("Authorization") token: String,
        @Body request: CreateDeliveryRequest
    ): Response<CreateDeliveryResponse>
    */

    /** @PUT("deliveries/{id}")
    suspend fun updateDelivery(
        @Header("Authorization") token: String,
        @Path("id") deliveryId: String,
        @Body request: UpdateDeliveryRequest
    ): Response<UpdateDeliveryResponse>
    */

    /** @DELETE("deliveries/{id}")
    suspend fun deleteDelivery(
        @Header("Authorization") token: String,
        @Path("id") deliveryId: String
    ): Response<DeleteDeliveryResponse>
    */

    @POST("deliveries/{id}/report-issue")
    suspend fun reportDeliveryIssue(
        @Header("Authorization") token: String,
        @Path("id") deliveryId: String,
        @Body request: ReportIssueRequest
    ): Response<ReportIssueResponse>

    @GET("deliveries/{id}/receipt")
    suspend fun getDeliveryReceipt(
        @Header("Authorization") token: String,
        @Path("id") deliveryId: String
    ): Response<DeliveryReceiptResponse>

    @GET("deliveries/{id}/receipt/pdf")
    @Streaming
    suspend fun downloadDeliveryReceiptPdf(
        @Header("Authorization") token: String,
        @Path("id") deliveryId: String
    ): Response<okhttp3.ResponseBody>

    @GET("deliveries/export/csv")
    @Streaming
    suspend fun exportDeliveriesToCsv(
        @Header("Authorization") token: String,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null
    ): Response<okhttp3.ResponseBody>

    @GET("deliveries/export/pdf")
    @Streaming
    suspend fun exportDeliveriesToPdf(
        @Header("Authorization") token: String,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null
    ): Response<okhttp3.ResponseBody>

    /**@POST("deliveries/sync")
    suspend fun syncDeliveries(
        @Header("Authorization") token: String,
        @Body request: SyncDeliveriesRequest
    ): Response<SyncDeliveriesResponse>
    */

    @GET("deliveries/price-history")
    suspend fun getPriceHistory(
        @Header("Authorization") token: String,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null
    ): Response<PriceHistoryResponse>

    @GET("deliveries/current-price")
    suspend fun getCurrentPrice(
        @Header("Authorization") token: String
    ): Response<CurrentPriceResponse>
}