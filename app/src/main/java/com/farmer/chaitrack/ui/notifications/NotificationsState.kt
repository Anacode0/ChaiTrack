package com.farmer.chaitrack.ui.notifications

import com.farmer.chaitrack.domain.model.NotificationItem
import com.farmer.chaitrack.domain.usecase.notification.NotificationPriority
import com.farmer.chaitrack.domain.usecase.notification.NotificationType

/**
 * UI State for the Notifications screen
 */
data class NotificationsState(
    val isLoading: Boolean = false,
    val notifications: List<NotificationItem> = emptyList(),
    val groupedNotifications: Map<String, List<NotificationItem>> = emptyMap(),
    val filteredNotifications: List<NotificationItem> = emptyList(),
    val selectedFilter: NotificationFilter = NotificationFilter.ALL,
    val selectedType: NotificationType? = null,
    val selectedPriority: NotificationPriority? = null,
    val unreadCount: Int = 0,
    val selectedNotificationIds: Set<String> = emptySet(),
    val isSelectionMode: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val isEmpty: Boolean = false,
    val showMarkAllAsReadButton: Boolean = false
) {
    /**
     * Gets the notifications to display based on filters
     */
    fun getDisplayNotifications(): List<NotificationItem> {
        return if (hasActiveFilters()) {
            filteredNotifications
        } else {
            notifications
        }
    }

    /**
     * Gets the grouped notifications for display
     */
    fun getGroupedDisplayNotifications(): Map<String, List<NotificationItem>> {
        return if (hasActiveFilters()) {
            groupFilteredNotifications()
        } else {
            groupedNotifications
        }
    }

    /**
     * Groups filtered notifications by date
     */
    private fun groupFilteredNotifications(): Map<String, List<NotificationItem>> {
        val now = System.currentTimeMillis()
        val oneDayMs = 24 * 60 * 60 * 1000L
        val oneWeekMs = 7 * oneDayMs

        val grouped = mutableMapOf<String, MutableList<NotificationItem>>()

        filteredNotifications.forEach { notification ->
            val age = now - notification.timestamp
            val key = when {
                age < oneDayMs -> "Today"
                age < 2 * oneDayMs -> "Yesterday"
                age < oneWeekMs -> "This Week"
                else -> "Older"
            }
            grouped.getOrPut(key) { mutableListOf() }.add(notification)
        }

        return grouped
    }

    /**
     * Checks if a notification is selected
     */
    fun isNotificationSelected(notificationId: String): Boolean {
        return selectedNotificationIds.contains(notificationId)
    }

    /**
     * Gets the selection count
     */
    fun getSelectionCount(): Int {
        return selectedNotificationIds.size
    }

    /**
     * Checks if all notifications are selected
     */
    fun areAllSelected(): Boolean {
        return selectedNotificationIds.size == getDisplayNotifications().size &&
                getDisplayNotifications().isNotEmpty()
    }

    /**
     * Checks if the state represents an error scenario
     */
    fun hasError(): Boolean {
        return error != null
    }

    /**
     * Checks if there are any notifications to display
     */
    fun hasNotifications(): Boolean {
        return getDisplayNotifications().isNotEmpty()
    }

    /**
     * Gets the empty state message
     */
    fun getEmptyStateMessage(): String {
        return when {
            selectedFilter == NotificationFilter.UNREAD -> "No unread notifications"
            selectedType != null -> "No ${selectedType.displayName.lowercase()} notifications"
            selectedPriority != null -> "No ${selectedPriority.name.lowercase()} priority notifications"
            else -> "No notifications yet"
        }
    }

    /**
     * Checks if filters are applied
     */
    fun hasActiveFilters(): Boolean {
        return selectedFilter != NotificationFilter.ALL ||
                selectedType != null ||
                selectedPriority != null
    }

    /**
     * Gets the filter summary text
     */
    fun getFilterSummary(): String {
        val filters = mutableListOf<String>()

        if (selectedFilter != NotificationFilter.ALL) {
            filters.add(selectedFilter.displayName)
        }
        if (selectedType != null) {
            filters.add(selectedType.displayName)
        }
        if (selectedPriority != null) {
            filters.add(selectedPriority.name)
        }

        return if (filters.isEmpty()) {
            "All Notifications"
        } else {
            filters.joinToString(" • ")
        }
    }
}

/**
 * Filter options for notifications
 */
enum class NotificationFilter(val displayName: String) {
    ALL("All"),
    UNREAD("Unread"),
    READ("Read"),
    TODAY("Today"),
    THIS_WEEK("This Week");

    companion object {
        fun getFilterOptions(): List<NotificationFilter> {
            return values().toList()
        }
    }
}

/**
 * Events that can occur on the Notifications screen
 */
sealed class NotificationsEvent {
    /**
     * Load notifications
     */
    object LoadNotifications : NotificationsEvent()

    /**
     * Refresh notifications from remote
     */
    object RefreshNotifications : NotificationsEvent()

    /**
     * Mark a notification as read
     */
    data class MarkAsRead(val notificationId: String) : NotificationsEvent()

    /**
     * Mark multiple notifications as read
     */
    data class MarkMultipleAsRead(val notificationIds: Set<String>) : NotificationsEvent()

    /**
     * Mark all notifications as read
     */
    object MarkAllAsRead : NotificationsEvent()

    /**
     * Mark a notification as unread
     */
    data class MarkAsUnread(val notificationId: String) : NotificationsEvent()

    /**
     * Delete a notification
     */
    data class DeleteNotification(val notificationId: String) : NotificationsEvent()

    /**
     * Delete multiple notifications
     */
    data class DeleteMultipleNotifications(val notificationIds: Set<String>) : NotificationsEvent()

    /**
     * Delete all notifications
     */
    object DeleteAllNotifications : NotificationsEvent()

    /**
     * Apply a filter
     */
    data class ApplyFilter(val filter: NotificationFilter) : NotificationsEvent()

    /**
     * Filter by type
     */
    data class FilterByType(val type: NotificationType?) : NotificationsEvent()

    /**
     * Filter by priority
     */
    data class FilterByPriority(val priority: NotificationPriority?) : NotificationsEvent()

    /**
     * Clear all filters
     */
    object ClearFilters : NotificationsEvent()

    /**
     * Toggle selection mode
     */
    object ToggleSelectionMode : NotificationsEvent()

    /**
     * Select/deselect a notification
     */
    data class ToggleNotificationSelection(val notificationId: String) : NotificationsEvent()

    /**
     * Select all notifications
     */
    object SelectAll : NotificationsEvent()

    /**
     * Deselect all notifications
     */
    object DeselectAll : NotificationsEvent()

    /**
     * Click on a notification
     */
    data class OnNotificationClick(val notification: NotificationItem) : NotificationsEvent()

    /**
     * Retry loading after error
     */
    object RetryLoading : NotificationsEvent()

    /**
     * Dismiss error message
     */
    object DismissError : NotificationsEvent()
}

/**
 * Side effects for the Notifications screen
 */
sealed class NotificationsSideEffect {
    /**
     * Show a toast message
     */
    data class ShowToast(val message: String) : NotificationsSideEffect()

    /**
     * Navigate to notification detail/action
     */
    data class NavigateToAction(val notification: NotificationItem) : NotificationsSideEffect()

    /**
     * Navigate to delivery screen
     */
    data class NavigateToDelivery(val deliveryId: String) : NotificationsSideEffect()

    /**
     * Navigate to fertilizer screen
     */
    data class NavigateToFertilizer(val applicationId: String) : NotificationsSideEffect()

    /**
     * Navigate to finance screen
     */
    data class NavigateToFinance(val recordId: String) : NotificationsSideEffect()

    /**
     * Show confirmation dialog
     */
    data class ShowConfirmationDialog(
        val title: String,
        val message: String,
        val onConfirm: () -> Unit
    ) : NotificationsSideEffect()

    /**
     * Update badge count
     */
    data class UpdateBadgeCount(val count: Int) : NotificationsSideEffect()
}