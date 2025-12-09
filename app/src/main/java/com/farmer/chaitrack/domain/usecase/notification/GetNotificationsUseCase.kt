package com.farmer.chaitrack.domain.usecase.notification

import com.farmer.chaitrack.data.repository.NotificationRepository
import com.farmer.chaitrack.domain.model.NotificationItem
import com.farmer.chaitrack.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetNotificationsUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    operator fun invoke(
        forceRefresh: Boolean = false
    ): Flow<Result<List<NotificationItem>>> = flow {
        emit(Result.Loading)

        try {
            if (forceRefresh) {
                notificationRepository.fetchAndSyncNotifications()
            }
            val notificationsFlow = notificationRepository.getNotificationsFlow()
            notificationsFlow.collect { notifications ->
                emit(Result.Success(notifications))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Failed to load notifications. Please try again."))
        }
    }

    fun getUnreadNotifications(): Flow<Result<List<NotificationItem>>> = flow {
        emit(Result.Loading)

        try {
            val unreadFlow = notificationRepository.getUnreadNotificationsFlow()
            unreadFlow.collect { notifications ->
                emit(Result.Success(notifications))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Failed to load unread notifications"))
        }
    }

    fun observeUnreadCount(): Flow<Int> {
        return notificationRepository.getUnreadCountFlow()
    }

    suspend fun getUnreadCount(): Int {
        return notificationRepository.getUnreadCount()
    }

    fun getNotificationsByType(type: NotificationType): Flow<Result<List<NotificationItem>>> = flow {
        emit(Result.Loading)

        try {
            val typeFlow = notificationRepository.getNotificationsByType(type.name)
            typeFlow.collect { notifications ->
                emit(Result.Success(notifications))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Failed to load notifications"))
        }
    }

    fun getNotificationsByPriority(priority: NotificationPriority): Flow<Result<List<NotificationItem>>> = flow {
        emit(Result.Loading)

        try {
            val notificationsFlow = notificationRepository.getNotificationsFlow()
            notificationsFlow.collect { notifications ->
                val filteredNotifications = notifications.filter {
                    true
                }
                emit(Result.Success(filteredNotifications))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Failed to load notifications"))
        }
    }

    fun getRecentNotifications(limit: Int = 20): Flow<Result<List<NotificationItem>>> = flow {
        emit(Result.Loading)

        try {
            val recentFlow = notificationRepository.getRecentNotifications(limit)
            recentFlow.collect { notifications ->
                emit(Result.Success(notifications))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Failed to load recent notifications"))
        }
    }

    fun filterByReadStatus(notifications: List<NotificationItem>, isRead: Boolean): List<NotificationItem> {
        return notifications.filter { it.isRead == isRead }
    }

    fun sortByDate(notifications: List<NotificationItem>): List<NotificationItem> {
        return notifications.sortedByDescending { it.timestamp }
    }

    fun groupNotificationsByDate(notifications: List<NotificationItem>): Map<String, List<NotificationItem>> {
        val now = System.currentTimeMillis()
        val oneDayMs = 24 * 60 * 60 * 1000L
        val oneWeekMs = 7 * oneDayMs

        val today = mutableListOf<NotificationItem>()
        val yesterday = mutableListOf<NotificationItem>()
        val thisWeek = mutableListOf<NotificationItem>()
        val older = mutableListOf<NotificationItem>()

        notifications.forEach { notification ->
            val age = now - notification.timestamp
            when {
                age < oneDayMs -> today.add(notification)
                age < 2 * oneDayMs -> yesterday.add(notification)
                age < oneWeekMs -> thisWeek.add(notification)
                else -> older.add(notification)
            }
        }

        return buildMap {
            if (today.isNotEmpty()) put("Today", today)
            if (yesterday.isNotEmpty()) put("Yesterday", yesterday)
            if (thisWeek.isNotEmpty()) put("This Week", thisWeek)
            if (older.isNotEmpty()) put("Older", older)
        }
    }

    suspend fun hasUnreadImportantNotifications(): Boolean {
        return try {
            val unreadFlow = notificationRepository.getUnreadNotificationsFlow()
            var hasImportant = false
            unreadFlow.collect { notifications ->
                hasImportant = notifications.any {
                    !it.isRead
                }
            }
            hasImportant
        } catch (e: Exception) {
            false
        }
    }

    fun getCountByType(notifications: List<NotificationItem>): Map<NotificationType, Int> {
        return notifications.groupBy { it.type }
            .mapValues { it.value.size }
    }
}