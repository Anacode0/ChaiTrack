package com.farmer.chaitrack.ui.delivery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmer.chaitrack.domain.model.DeliveryItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar

class DeliveryViewModel : ViewModel() {

    private val _state = MutableStateFlow(DeliveryState())
    val state: StateFlow<DeliveryState> = _state.asStateFlow()

    fun loadDeliveries(filter: DeliveryFilter) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                // TODO: Replace with actual repository call
                kotlinx.coroutines.delay(800)

                val deliveries = getDeliveriesByFilter(filter)
                val total = deliveries.sumOf { it.weight }

                _state.update {
                    it.copy(
                        isLoading = false,
                        deliveries = deliveries,
                        currentFilter = filter,
                        totalWeight = total
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load deliveries"
                    )
                }
            }
        }
    }

    fun loadDeliveriesByYear(year: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                // TODO: Replace with actual repository call
                kotlinx.coroutines.delay(800)

                val deliveries = getDeliveriesByYear(year)
                val total = deliveries.sumOf { it.weight }

                _state.update {
                    it.copy(
                        isLoading = false,
                        deliveries = deliveries,
                        selectedYear = year,
                        totalWeight = total
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load deliveries"
                    )
                }
            }
        }
    }

    fun reportIssue(description: String) {
        viewModelScope.launch {
            try {
                // TODO: Call repository to submit issue report
                kotlinx.coroutines.delay(500)

                _state.update { it.copy(issueReported = true) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(error = e.message ?: "Failed to report issue")
                }
            }
        }
    }

    fun resetIssueReportedState() {
        _state.update { it.copy(issueReported = false) }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }

    fun getFirstLoginYear(): Int {
        // TODO: Get from PreferencesManager or UserRepository
        return 2023
    }

    // TODO: Replace with actual repository calls
    private fun getDeliveriesByFilter(filter: DeliveryFilter): List<DeliveryItem> {
        val calendar = Calendar.getInstance()
        val now = calendar.timeInMillis

        val startDate = when (filter) {
            DeliveryFilter.ALL -> {
                0L // Very old date for ALL filter
            }
            DeliveryFilter.THIS_MONTH -> {
                calendar.set(Calendar.DAY_OF_MONTH, 1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                calendar.timeInMillis
            }
            DeliveryFilter.LAST_TWO_WEEKS -> {
                calendar.add(Calendar.WEEK_OF_YEAR, -2)
                calendar.timeInMillis
            }
            DeliveryFilter.LAST_MONTH -> {
                calendar.add(Calendar.MONTH, -1)
                calendar.timeInMillis
            }
            DeliveryFilter.LAST_THREE_MONTHS -> {
                calendar.add(Calendar.MONTH, -3)
                calendar.timeInMillis
            }
            DeliveryFilter.LAST_SIX_MONTHS -> {
                calendar.add(Calendar.MONTH, -6)
                calendar.timeInMillis
            }
            DeliveryFilter.LAST_YEAR -> {
                calendar.add(Calendar.YEAR, -1)
                calendar.timeInMillis
            }
        }

        // Sample data - replace with actual repository call
        return generateSampleDeliveries(startDate, now)
    }

    private fun getDeliveriesByYear(year: Int): List<DeliveryItem> {
        // TODO: Call repository to get deliveries for specific year
        val calendar = Calendar.getInstance()
        calendar.set(year, Calendar.JANUARY, 1, 0, 0, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startDate = calendar.timeInMillis

        calendar.set(year, Calendar.DECEMBER, 31, 23, 59, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val endDate = calendar.timeInMillis

        return generateSampleDeliveries(startDate, endDate)
    }

    private fun generateSampleDeliveries(startDate: Long, endDate: Long): List<DeliveryItem> {
        // Sample data generator - replace with actual repository call
        return listOf(
            DeliveryItem(
                id = "1",
                userId = "user123", // Added required userId parameter
                clerkName = "Jane D",
                weight = 45.5,
                date = System.currentTimeMillis(), // Use Long timestamp instead of Date
                time = "08:30 AM",
                monthTotal = 1250.50
            ),
            DeliveryItem(
                id = "2",
                userId = "user123",
                clerkName = "John Kamau",
                weight = 52.0,
                date = System.currentTimeMillis() - 86400000L, // Yesterday
                time = "09:15 AM",
                monthTotal = 1205.0
            ),
            DeliveryItem(
                id = "3",
                userId = "user123",
                clerkName = "Mary Kironji",
                weight = 48.5,
                date = System.currentTimeMillis() - 172800000L, // 2 days ago
                time = "07:45 AM",
                monthTotal = 1153.0
            ),
            DeliveryItem(
                id = "4",
                userId = "user123",
                clerkName = "Jane Mohammed",
                weight = 50.0,
                date = System.currentTimeMillis() - 259200000L, // 3 days ago
                time = "08:00 AM",
                monthTotal = 1104.5
            ),
            DeliveryItem(
                id = "5",
                userId = "user123",
                clerkName = "John Kerubo",
                weight = 46.5,
                date = System.currentTimeMillis() - 345600000L, // 4 days ago
                time = "09:30 AM",
                monthTotal = 1054.5
            )
        ).sortedByDescending { it.date }
    }
}