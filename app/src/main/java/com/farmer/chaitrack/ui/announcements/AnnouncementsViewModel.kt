package com.farmer.chaitrack.ui.announcements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmer.chaitrack.domain.model.Announcement
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AnnouncementsViewModel : ViewModel() {

    private val _state = MutableStateFlow(AnnouncementsState())
    val state: StateFlow<AnnouncementsState> = _state.asStateFlow()

    fun loadAnnouncements() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                kotlinx.coroutines.delay(800)

                val announcements = getAnnouncementsFromRepository()

                _state.update {
                    it.copy(
                        isLoading = false,
                        announcements = announcements
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load announcements"
                    )
                }
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }
    private fun getAnnouncementsFromRepository(): List<Announcement> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

        return listOf(
            Announcement(
                id = "1",
                title = "New Fertilizer Rates",
                body = "Please be advised that the rates for Grade A tea have been updated effective immediately. The new pricing structure will affect all deliveries from November onwards. Please contact your local supervisor for more details about the changes and how they will impact your earnings.",
                content = "Please be advised that the rates for Grade A tea have been updated effective immediately. The new pricing structure will affect all deliveries from November onwards. Please contact your local supervisor for more details about the changes and how they will impact your earnings.",
                date = dateFormat.format(Date(System.currentTimeMillis() - 86400000L)), // Yesterday
                category = "PRICING",
                isRead = false,
                isUrgent = false
            ),
            Announcement(
                id = "2",
                title = "Holiday Schedule",
                body = "Our offices will be closed on December 25th and 26th for the holiday season. Normal operations will resume on December 27th. Emergency support will be available via the hotline.",
                content = "Our offices will be closed on December 25th and 26th for the holiday season. Normal operations will resume on December 27th. Emergency support will be available via the hotline.",
                date = dateFormat.format(Date(System.currentTimeMillis() - 172800000L)), // 2 days ago
                category = "OPERATIONS",
                isRead = false,
                isUrgent = false
            ),
            Announcement(
                id = "3",
                title = "Quality Standards Update",
                body = "We have updated our quality standards for tea leaf collection. Please ensure all deliveries meet the new moisture content requirements. Training sessions will be held next week at all collection centers.",
                content = "We have updated our quality standards for tea leaf collection. Please ensure all deliveries meet the new moisture content requirements. Training sessions will be held next week at all collection centers.",
                date = dateFormat.format(Date(System.currentTimeMillis() - 604800000L)), // 1 week ago
                category = "QUALITY",
                isRead = false,
                isUrgent = true
            )
        )
    }
}