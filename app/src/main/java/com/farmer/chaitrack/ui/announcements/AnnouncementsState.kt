package com.farmer.chaitrack.ui.announcements

import com.farmer.chaitrack.domain.model.Announcement

data class AnnouncementsState(
    val isLoading: Boolean = false,
    val announcements: List<Announcement> = emptyList(),
    val filteredAnnouncements: List<Announcement> = emptyList(),
    val expandedAnnouncementId: String? = null,
    val selectedFilter: AnnouncementFilter = AnnouncementFilter.ALL,
    val searchQuery: String = "",
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val isEmpty: Boolean = false,
    val hasUnreadAnnouncements: Boolean = false,
    val unreadCount: Int = 0
) {
    fun getDisplayAnnouncements(): List<Announcement> {
        return if (searchQuery.isBlank() && selectedFilter == AnnouncementFilter.ALL) {
            announcements
        } else {
            filteredAnnouncements
        }
    }

    fun isAnnouncementExpanded(announcementId: String): Boolean {
        return expandedAnnouncementId == announcementId
    }

    fun hasError(): Boolean {
        return error != null
    }

    fun hasAnnouncements(): Boolean {
        return getDisplayAnnouncements().isNotEmpty()
    }

    fun getEmptyStateMessage(): String {
        return when {
            searchQuery.isNotBlank() -> "No announcements found matching \"$searchQuery\""
            selectedFilter != AnnouncementFilter.ALL -> "No ${selectedFilter.displayName.lowercase()} announcements"
            else -> "No announcements yet"
        }
    }

    fun hasActiveFilters(): Boolean {
        return selectedFilter != AnnouncementFilter.ALL || searchQuery.isNotBlank()
    }
}

enum class AnnouncementFilter(val displayName: String) {
    ALL("All"),
    UNREAD("Unread"),
    IMPORTANT("Important"),
    GENERAL("General"),
    FERTILIZER("Fertilizer"),
    PAYMENT("Payment"),
    SYSTEM("System");

    companion object {
        fun getFilterOptions(): List<AnnouncementFilter> {
            return listOf(ALL, UNREAD, IMPORTANT)
        }

        fun getCategoryFilters(): List<AnnouncementFilter> {
            return listOf(GENERAL, FERTILIZER, PAYMENT, SYSTEM)
        }
    }
}
sealed class AnnouncementsEvent {
    object LoadAnnouncements : AnnouncementsEvent()
    object RefreshAnnouncements : AnnouncementsEvent()

    data class ToggleAnnouncementExpansion(val announcementId: String) : AnnouncementsEvent()

    data class MarkAsRead(val announcementId: String) : AnnouncementsEvent()

    object MarkAllAsRead : AnnouncementsEvent()

    data class ApplyFilter(val filter: AnnouncementFilter) : AnnouncementsEvent()

    data class SearchAnnouncements(val query: String) : AnnouncementsEvent()

    object ClearFilters : AnnouncementsEvent()

    data class DeleteAnnouncement(val announcementId: String) : AnnouncementsEvent()

    data class ShareAnnouncement(val announcement: Announcement) : AnnouncementsEvent()

    object RetryLoading : AnnouncementsEvent()

    object DismissError : AnnouncementsEvent()
}

sealed class AnnouncementsSideEffect {
    data class ShowToast(val message: String) : AnnouncementsSideEffect()
    data class NavigateToDetails(val announcement: Announcement) : AnnouncementsSideEffect()
    data class OpenShareSheet(val shareText: String) : AnnouncementsSideEffect()
    data class ShowConfirmationDialog(
        val title: String,
        val message: String,
        val onConfirm: () -> Unit
    ) : AnnouncementsSideEffect()
}