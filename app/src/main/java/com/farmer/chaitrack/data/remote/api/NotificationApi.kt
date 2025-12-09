package com.farmer.chaitrack.data.remote.api

import com.farmer.chaitrack.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface NotificationApi {

    @GET("notifications")
    suspend fun getNotifications(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 50
    ): Response<NotificationsResponse>

    @GET("notifications/{id}")
    suspend fun getNotificationById(
        @Header("Authorization") token: String,
        @Path("id") notificationId: String
    ): Response<NotificationDetailResponse>

    @GET("notifications/unread")
    suspend fun getUnreadNotifications(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 50
    ): Response<NotificationsResponse>

    @GET("notifications/unread/count")
    suspend fun getUnreadCount(
        @Header("Authorization") token: String
    ): Response<UnreadCountResponse>

    @GET("notifications/type")
    suspend fun getNotificationsByType(
        @Header("Authorization") token: String,
        @Query("type") type: String, // DELIVERY, FERTILIZER, FINANCE, ANNOUNCEMENT, SYSTEM
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 50
    ): Response<NotificationsResponse>

    @GET("notifications/priority")
    suspend fun getNotificationsByPriority(
        @Header("Authorization") token: String,
        @Query("priority") priority: String, // LOW, NORMAL, HIGH, URGENT
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 50
    ): Response<NotificationsResponse>

    @GET("notifications/recent")
    suspend fun getRecentNotifications(
        @Header("Authorization") token: String,
        @Query("days") days: Int = 7,
        @Query("limit") limit: Int = 20
    ): Response<NotificationsResponse>

    @PUT("notifications/{id}/read")
    suspend fun markAsRead(
        @Header("Authorization") token: String,
        @Path("id") notificationId: String
    ): Response<MarkAsReadResponse>

    @PUT("notifications/read/batch")
    suspend fun markMultipleAsRead(
        @Header("Authorization") token: String,
        @Body request: MarkMultipleAsReadRequest
    ): Response<MarkMultipleAsReadResponse>

    @PUT("notifications/read/all")
    suspend fun markAllAsRead(
        @Header("Authorization") token: String
    ): Response<MarkAllAsReadResponse>

    @DELETE("notifications/{id}")
    suspend fun deleteNotification(
        @Header("Authorization") token: String,
        @Path("id") notificationId: String
    ): Response<DeleteNotificationResponse>

    @POST("notifications/delete/batch")
    suspend fun deleteMultipleNotifications(
        @Header("Authorization") token: String,
        @Body request: DeleteMultipleNotificationsRequest
    ): Response<DeleteMultipleNotificationsResponse>

    @DELETE("notifications/read")
    suspend fun deleteAllReadNotifications(
        @Header("Authorization") token: String
    ): Response<DeleteAllReadNotificationsResponse>

    @DELETE("notifications/all")
    suspend fun clearAllNotifications(
        @Header("Authorization") token: String
    ): Response<ClearAllNotificationsResponse>

    @GET("notifications/preferences")
    suspend fun getNotificationPreferences(
        @Header("Authorization") token: String
    ): Response<NotificationPreferences>

    @PUT("notifications/preferences")
    suspend fun updateNotificationPreferences(
        @Header("Authorization") token: String,
        @Body request: UpdateNotificationPreferencesRequest
    ): Response<UpdateNotificationPreferencesResponse>

    @POST("notifications/fcm-token")
    suspend fun registerFcmToken(
        @Header("Authorization") token: String,
        @Body request: RegisterFcmTokenRequest
    ): Response<RegisterFcmTokenResponse>

    @DELETE("notifications/fcm-token")
    suspend fun unregisterFcmToken(
        @Header("Authorization") token: String,
        @Body request: UnregisterFcmTokenRequest
    ): Response<UnregisterFcmTokenResponse>

    @POST("notifications/test")
    suspend fun sendTestNotification(
        @Header("Authorization") token: String
    ): Response<TestNotificationResponse>

    @GET("notifications/archive")
    suspend fun getNotificationArchive(
        @Header("Authorization") token: String,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 50
    ): Response<NotificationsResponse>

    @POST("notifications/{id}/snooze")
    suspend fun snoozeNotification(
        @Header("Authorization") token: String,
        @Path("id") notificationId: String,
        @Body request: SnoozeNotificationRequest
    ): Response<SnoozeNotificationResponse>

    @GET("notifications/statistics")
    suspend fun getNotificationStatistics(
        @Header("Authorization") token: String
    ): Response<NotificationStatistics>

    @POST("notifications/subscribe")
    suspend fun subscribeToCategory(
        @Header("Authorization") token: String,
        @Body request: SubscribeToCategoryRequest
    ): Response<SubscribeToCategoryResponse>

    @POST("notifications/unsubscribe")
    suspend fun unsubscribeFromCategory(
        @Header("Authorization") token: String,
        @Body request: UnsubscribeFromCategoryRequest
    ): Response<UnsubscribeFromCategoryResponse>

    @GET("notifications/subscriptions")
    suspend fun getSubscribedCategories(
        @Header("Authorization") token: String
    ): Response<SubscribedCategoriesResponse>
}