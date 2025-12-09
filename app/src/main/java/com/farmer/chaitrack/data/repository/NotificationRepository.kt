package com.farmer.chaitrack.data.repository

import com.farmer.chaitrack.data.local.database.dao.NotificationDao
import com.farmer.chaitrack.data.local.entity.NotificationEntity
import com.farmer.chaitrack.data.local.preferences.PreferencesManager
import com.farmer.chaitrack.data.remote.api.NotificationApi
import com.farmer.chaitrack.data.remote.dto.*
import com.farmer.chaitrack.domain.model.NotificationItem
import com.farmer.chaitrack.domain.usecase.notification.NotificationType
import com.farmer.chaitrack.util.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class NotificationRepository @Inject constructor(
    private val notificationApi: NotificationApi,
    private val notificationDao: NotificationDao,
    private val preferencesManager: PreferencesManager,
    private val dispatcher: CoroutineDispatcher
) {

    fun getNotificationsFlow(): Flow<List<NotificationItem>> {
        return notificationDao.getAllNotifications().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    fun getUnreadNotificationsFlow(): Flow<List<NotificationItem>> {
        return notificationDao.getUnreadNotifications().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    fun getUnreadCountFlow(): Flow<Int> {
        return notificationDao.getUnreadNotificationCount()
    }

    suspend fun getUnreadCount(): Int {
        return withContext(dispatcher) {
            notificationDao.getUnreadNotificationCountOnce()
        }
    }

    fun getNotificationsByType(type: String): Flow<List<NotificationItem>> {
        return notificationDao.getNotificationsByType(type).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    fun getRecentNotifications(limit: Int = 20): Flow<List<NotificationItem>> {
        return notificationDao.getRecentNotifications(limit).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    suspend fun fetchAndSyncNotifications(): Result<List<NotificationItem>> {
        return withContext(dispatcher) {
            try {
                val token = preferencesManager.getAuthToken()
                    ?: return@withContext Result.Error("Not authenticated")

                val response = notificationApi.getNotifications("Bearer $token")

                if (response.isSuccessful && response.body()?.success == true) {
                    val notifications = response.body()?.data?.notifications ?: emptyList()

                    val entities = notifications.map { it.toEntity() }
                    notificationDao.insertNotifications(entities)

                    preferencesManager.updateLastSyncTime()

                    Result.Success(entities.map { it.toDomainModel() })
                } else {
                    Result.Error("Failed to fetch notifications")
                }
            } catch (e: Exception) {
                Result.Error(e.message ?: "Network error occurred")
            }
        }
    }

    suspend fun markAsRead(notificationId: String): Result<Boolean> {
        return withContext(dispatcher) {
            try {
                notificationDao.markAsRead(notificationId)

                val token = preferencesManager.getAuthToken()
                if (token != null) {
                    val response = notificationApi.markAsRead("Bearer $token", notificationId)
                    if (response.isSuccessful && response.body()?.success == true) {
                        // Server sync successful
                    }
                }

                Result.Success(true)
            } catch (e: Exception) {
                Result.Error("Failed to mark as read")
            }
        }
    }

    suspend fun markMultipleAsRead(notificationIds: List<String>): Result<Boolean> {
        return withContext(dispatcher) {
            try {
                notificationDao.markMultipleAsRead(notificationIds)

                val token = preferencesManager.getAuthToken()
                if (token != null) {
                    val request = MarkMultipleAsReadRequest(notificationIds = notificationIds)
                    val response = notificationApi.markMultipleAsRead("Bearer $token", request)
                    if (response.isSuccessful && response.body()?.success == true) {
                        // Server sync successful
                    }
                }

                Result.Success(true)
            } catch (e: Exception) {
                Result.Error("Failed to mark notifications as read")
            }
        }
    }

    suspend fun markAllAsRead(): Result<Boolean> {
        return withContext(dispatcher) {
            try {
                notificationDao.markAllAsRead()

                val token = preferencesManager.getAuthToken()
                if (token != null) {
                    val response = notificationApi.markAllAsRead("Bearer $token")
                    if (response.isSuccessful && response.body()?.success == true) {
                        // Server sync successful
                    }
                }

                Result.Success(true)
            } catch (e: Exception) {
                Result.Error("Failed to mark all as read")
            }
        }
    }

    suspend fun deleteNotification(notificationId: String): Result<Boolean> {
        return withContext(dispatcher) {
            try {
                notificationDao.deleteNotificationById(notificationId)

                val token = preferencesManager.getAuthToken()
                if (token != null) {
                    val response = notificationApi.deleteNotification("Bearer $token", notificationId)
                    if (response.isSuccessful && response.body()?.success == true) {
                        // Server sync successful
                    }
                }

                Result.Success(true)
            } catch (e: Exception) {
                Result.Error("Failed to delete notification")
            }
        }
    }

    suspend fun deleteAllReadNotifications(): Result<Boolean> {
        return withContext(dispatcher) {
            try {
                val thirtyDaysAgo = System.currentTimeMillis() - (30 * 24 * 60 * 60 * 1000L)

                notificationDao.deleteOldReadNotifications(thirtyDaysAgo)

                val token = preferencesManager.getAuthToken()
                if (token != null) {
                    val response = notificationApi.deleteAllReadNotifications("Bearer $token")
                    if (response.isSuccessful && response.body()?.success == true) {
                        // Server sync successful
                    }
                }

                Result.Success(true)
            } catch (e: Exception) {
                Result.Error("Failed to delete notifications")
            }
        }
    }

    suspend fun clearAllNotifications(): Result<Boolean> {
        return withContext(dispatcher) {
            try {
                notificationDao.deleteAllNotifications()

                val token = preferencesManager.getAuthToken()
                if (token != null) {
                    val response = notificationApi.clearAllNotifications("Bearer $token")
                    if (response.isSuccessful && response.body()?.success == true) {
                        // Server sync successful
                    }
                }

                Result.Success(true)
            } catch (e: Exception) {
                Result.Error("Failed to clear notifications")
            }
        }
    }

    suspend fun registerFcmToken(fcmToken: String): Result<Boolean> {
        return withContext(dispatcher) {
            try {
                val token = preferencesManager.getAuthToken()
                    ?: return@withContext Result.Error("Not authenticated")

                val deviceId = preferencesManager.getDeviceId() ?: generateDeviceId()

                val request = RegisterFcmTokenRequest(
                    fcmToken = fcmToken,
                    deviceId = deviceId
                )
                val response = notificationApi.registerFcmToken("Bearer $token", request)

                if (response.isSuccessful && response.body()?.success == true) {
                    Result.Success(true)
                } else {
                    Result.Error("Failed to register FCM token")
                }
            } catch (e: Exception) {
                Result.Error(e.message ?: "Network error occurred")
            }
        }
    }

    suspend fun updateNotificationPreferences(
        notificationsEnabled: Boolean,
        soundEnabled: Boolean,
        vibrationEnabled: Boolean
    ): Result<Boolean> {
        return withContext(dispatcher) {
            try {
                val token = preferencesManager.getAuthToken()
                    ?: return@withContext Result.Error("Not authenticated")

                val request = UpdateNotificationPreferencesRequest(
                    notificationsEnabled = notificationsEnabled,
                    soundEnabled = soundEnabled,
                    vibrationEnabled = vibrationEnabled
                )
                val response = notificationApi.updateNotificationPreferences("Bearer $token", request)

                if (response.isSuccessful && response.body()?.success == true) {
                    preferencesManager.setNotificationsEnabled(notificationsEnabled)
                    preferencesManager.setSoundEnabled(soundEnabled)
                    preferencesManager.setVibrationEnabled(vibrationEnabled)

                    Result.Success(true)
                } else {
                    Result.Error("Failed to update preferences")
                }
            } catch (e: Exception) {
                Result.Error(e.message ?: "Network error occurred")
            }
        }
    }

    suspend fun getNotificationStatistics(): Result<NotificationStatistics> {
        return withContext(dispatcher) {
            try {
                val token = preferencesManager.getAuthToken()
                    ?: return@withContext Result.Error("Not authenticated")

                val response = notificationApi.getNotificationStatistics("Bearer $token")

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.success == true) {
                        Result.Success(body)
                    } else {
                        Result.Error("Failed to fetch statistics: API response indicates failure")
                    }
                } else {
                    Result.Error("Failed to fetch statistics: HTTP ${response.code()}")
                }
            } catch (e: Exception) {
                Result.Error(
                    message = e.message ?: "Network error occurred",
                    throwable = e
                )
            }
        }
    }

    suspend fun cleanupOldNotifications() {
        withContext(dispatcher) {
            try {
                val thirtyDaysAgo = System.currentTimeMillis() - (30 * 24 * 60 * 60 * 1000L)
                notificationDao.deleteOldNotifications(thirtyDaysAgo)
            } catch (e: Exception) {
            }
        }
    }
    private fun NotificationEntity.toDomainModel(): NotificationItem {
        return NotificationItem(
            id = this.id,
            type = NotificationType.valueOf(this.type),
            title = this.title,
            message = this.message,
            timestamp = this.timestamp,
            isRead = this.isRead
        )
    }

    private fun NotificationDto.toEntity(): NotificationEntity {
        val userId = preferencesManager.getUserId() ?: ""
        return NotificationEntity(
            id = this.id,
            userId = userId,
            type = this.type,
            title = this.title,
            message = this.message,
            timestamp = parseDateToTimestamp(this.timestamp),
            isRead = this.isRead ?: false,
            readAt = parseDateToTimestamp(this.readAt),
            priority = this.priority ?: "NORMAL",
            actionType = this.actionType,
            actionData = this.actionData,
            iconUrl = this.iconUrl,
            imageUrl = this.imageUrl,
            category = this.category,
            expiresAt = parseDateToTimestamp(this.expiresAt),
            soundEnabled = this.soundEnabled ?: true,
            vibrationEnabled = this.vibrationEnabled ?: true,
            groupKey = this.groupKey,
            createdAt = parseDateToTimestamp(this.createdAt),
            updatedAt = parseDateToTimestamp(this.updatedAt),
            syncedToServer = true,
            serverId = this.id
        )
    }

    private fun parseDateToTimestamp(dateString: String?): Long {
        if (dateString == null) return System.currentTimeMillis()
        return try {
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            format.parse(dateString)?.time ?: System.currentTimeMillis()
        } catch (e: Exception) {
            System.currentTimeMillis()
        }
    }

    private fun generateDeviceId(): String {
        val deviceId = UUID.randomUUID().toString()
        preferencesManager.saveDeviceId(deviceId)
        return deviceId
    }

    suspend fun markAsUnread(notificationId: String): Result<Boolean> {
        return withContext(dispatcher) {
            try {
                notificationDao.markAsUnread(notificationId)

                val token = preferencesManager.getAuthToken()
                if (token != null) {
                    // val response = notificationApi.markAsUnread("Bearer $token", notificationId)
                    // Handle server response
                }

                Result.Success(true)
            } catch (e: Exception) {
                Result.Error("Failed to mark as unread")
            }
        }
    }

    suspend fun deleteMultipleNotifications(notificationIds: List<String>): Result<Boolean> {
        return withContext(dispatcher) {
            try {
                notificationDao.deleteMultipleNotifications(notificationIds)

                val token = preferencesManager.getAuthToken()
                if (token != null) {
                    val request = DeleteMultipleNotificationsRequest(notificationIds = notificationIds)
                    val response = notificationApi.deleteMultipleNotifications("Bearer $token", request)
                }

                Result.Success(true)
            } catch (e: Exception) {
                Result.Error("Failed to delete notifications")
            }
        }
    }

    suspend fun deleteNotificationsOlderThan(timestamp: Long): Result<Boolean> {
        return withContext(dispatcher) {
            try {
                notificationDao.deleteOldNotifications(timestamp)
                Result.Success(true)
            } catch (e: Exception) {
                Result.Error("Failed to delete old notifications")
            }
        }
    }

    suspend fun isNotificationRead(notificationId: String): Boolean {
        return withContext(dispatcher) {
            try {
                val notification = notificationDao.getNotificationById(notificationId)
                notification?.isRead ?: false
            } catch (e: Exception) {
                false
            }
        }
    }
}