package com.farmer.chaitrack.ui.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmer.chaitrack.domain.model.NotificationItem
import com.farmer.chaitrack.domain.usecase.notification.NotificationType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotificationsViewModel : ViewModel() {

    private val _state = MutableStateFlow(NotificationsState())
    val state: StateFlow<NotificationsState> = _state.asStateFlow()

    fun loadNotifications() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                // TODO: Replace with actual repository call
                kotlinx.coroutines.delay(800)

                val notifications = getNotificationsFromRepository()

                _state.update {
                    it.copy(
                        isLoading = false,
                        notifications = notifications
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load notifications"
                    )
                }
            }
        }
    }

    fun markAsRead(notificationId: String) {
        viewModelScope.launch {
            try {
                // TODO: Call repository to mark as read
                val updatedNotifications = _state.value.notifications.map { notification ->
                    if (notification.id == notificationId) {
                        notification.copy(isRead = true)
                    } else {
                        notification
                    }
                }

                _state.update {
                    it.copy(notifications = updatedNotifications)
                }
            } catch (e: Exception) {
                // Handle error silently
            }
        }
    }

    fun markAllAsRead() {
        viewModelScope.launch {
            try {
                // TODO: Call repository to mark all as read
                val updatedNotifications = _state.value.notifications.map { notification ->
                    notification.copy(isRead = true)
                }

                _state.update {
                    it.copy(notifications = updatedNotifications)
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(error = e.message ?: "Failed to mark notifications as read")
                }
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }

    // TODO: Replace with actual repository call
    private fun getNotificationsFromRepository(): List<NotificationItem> {
        // Sample data - using Long timestamps instead of Date objects
        return listOf(
            NotificationItem(
                id = "1",
                type = NotificationType.DELIVERY,
                title = "Delivery Confirmed",
                message = "Your delivery of 45.5 kg has been confirmed and recorded.",
                timestamp = System.currentTimeMillis() - 7200000L, // 2 hours ago
                isRead = false
            ),
            NotificationItem(
                id = "2",
                type = NotificationType.FERTILIZER,
                title = "Fertilizer Application Approved",
                message = "Your Spring 2024 fertilizer application has been approved. 50 bags ready for pickup.",
                timestamp = System.currentTimeMillis() - 86400000L, // 1 day ago
                isRead = false
            ),
            NotificationItem(
                id = "3",
                type = NotificationType.FINANCE,
                title = "Transcript Generated",
                message = "Your October 2024 salary transcript is now available for download.",
                timestamp = System.currentTimeMillis() - 172800000L, // 2 days ago
                isRead = true
            ),
            NotificationItem(
                id = "4",
                type = NotificationType.ANNOUNCEMENT,
                title = "New Fertilizer Rates",
                message = "Please be advised that the rates for Grade A tea have been updated.",
                timestamp = System.currentTimeMillis() - 259200000L, // 3 days ago
                isRead = true
            ),
            NotificationItem(
                id = "5",
                type = NotificationType.SYSTEM,
                title = "Welcome to ChaiTrack",
                message = "Thank you for using ChaiTrack. We're here to help you manage your tea farming operations.",
                timestamp = System.currentTimeMillis() - 604800000L, // 1 week ago
                isRead = true
            )
        )
    }
}