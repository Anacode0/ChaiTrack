package com.farmer.chaitrack.domain.usecase.notification

import com.farmer.chaitrack.data.repository.NotificationRepository
import com.farmer.chaitrack.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MarkAsReadUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    operator fun invoke(notificationId: String): Flow<Result<Unit>> = flow {
        emit(Result.Loading)

        if (notificationId.isBlank()) {
            emit(Result.Error("Invalid notification ID"))
            return@flow
        }

        try {
            val result = notificationRepository.markAsRead(notificationId)
            when (result) {
                is Result.Success -> emit(Result.Success(Unit))
                is Result.Error -> emit(Result.Error(result.message))
                else -> emit(Result.Error("Unknown result type"))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Failed to mark notification as read"))
        }
    }

    fun markMultipleAsRead(notificationIds: List<String>): Flow<Result<Unit>> = flow {
        emit(Result.Loading)

        if (notificationIds.isEmpty()) {
            emit(Result.Error("No notifications selected"))
            return@flow
        }

        try {
            val result = notificationRepository.markMultipleAsRead(notificationIds)
            when (result) {
                is Result.Success -> emit(Result.Success(Unit))
                is Result.Error -> emit(Result.Error(result.message))
                else -> emit(Result.Error("Unknown result type"))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Failed to mark notifications as read"))
        }
    }

    fun markAllAsRead(): Flow<Result<Unit>> = flow {
        emit(Result.Loading)

        try {
            val result = notificationRepository.markAllAsRead()
            when (result) {
                is Result.Success -> emit(Result.Success(Unit))
                is Result.Error -> emit(Result.Error(result.message))
                else -> emit(Result.Error("Unknown result type"))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Failed to mark all notifications as read"))
        }
    }

    fun markAsUnread(notificationId: String): Flow<Result<Unit>> = flow {
        emit(Result.Loading)

        if (notificationId.isBlank()) {
            emit(Result.Error("Invalid notification ID"))
            return@flow
        }

        try {
            emit(Result.Error("markAsUnread not implemented"))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Failed to mark notification as unread"))
        }
    }

    fun deleteNotification(notificationId: String): Flow<Result<Unit>> = flow {
        emit(Result.Loading)

        if (notificationId.isBlank()) {
            emit(Result.Error("Invalid notification ID"))
            return@flow
        }

        try {
            val result = notificationRepository.deleteNotification(notificationId)
            when (result) {
                is Result.Success -> emit(Result.Success(Unit))
                is Result.Error -> emit(Result.Error(result.message))
                else -> emit(Result.Error("Unknown result type"))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Failed to delete notification"))
        }
    }

    fun deleteMultipleNotifications(notificationIds: List<String>): Flow<Result<Unit>> = flow {
        emit(Result.Loading)

        if (notificationIds.isEmpty()) {
            emit(Result.Error("No notifications selected"))
            return@flow
        }

        try {
            emit(Result.Error("deleteMultipleNotifications not implemented"))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Failed to delete notifications"))
        }
    }

    fun clearOldNotifications(daysOld: Int = 30): Flow<Result<Unit>> = flow {
        emit(Result.Loading)

        if (daysOld < 1) {
            emit(Result.Error("Invalid number of days"))
            return@flow
        }

        try {
            notificationRepository.cleanupOldNotifications()
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Failed to clear old notifications"))
        }
    }

    fun toggleReadStatus(
        notificationId: String,
        currentReadStatus: Boolean
    ): Flow<Result<Unit>> = flow {
        emit(Result.Loading)

        if (notificationId.isBlank()) {
            emit(Result.Error("Invalid notification ID"))
            return@flow
        }

        try {
            val result = if (currentReadStatus) {
                Result.Error("markAsUnread not implemented")
            } else {
                notificationRepository.markAsRead(notificationId)
            }
            when (result) {
                is Result.Success -> emit(Result.Success(Unit))
                is Result.Error -> emit(Result.Error(result.message))
                else -> emit(Result.Error("Unknown result type"))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Failed to update notification status"))
        }
    }

    fun autoMarkAsReadOnView(
        notificationId: String,
        viewDurationMs: Long = 2000L
    ): Flow<Result<Unit>> = flow {
        emit(Result.Loading)

        if (notificationId.isBlank()) {
            emit(Result.Error("Invalid notification ID"))
            return@flow
        }

        try {
            kotlinx.coroutines.delay(viewDurationMs)

            val result = notificationRepository.markAsRead(notificationId)
            when (result) {
                is Result.Success -> emit(Result.Success(Unit))
                is Result.Error -> emit(Result.Error(result.message))
                else -> emit(Result.Error("Unknown result type"))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Failed to auto-mark notification as read"))
        }
    }
}