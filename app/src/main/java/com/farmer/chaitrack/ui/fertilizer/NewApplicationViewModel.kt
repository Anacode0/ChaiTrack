package com.farmer.chaitrack.ui.fertilizer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewApplicationViewModel : ViewModel() {

    private val _state = MutableStateFlow(NewApplicationState())
    val state: StateFlow<NewApplicationState> = _state.asStateFlow()

    // Track submission state to prevent multiple submissions
    private var isSubmitting = false

    fun loadFarmerData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                kotlinx.coroutines.delay(500)

                val plotSize = getPlotSizeFromProfile()

                _state.update {
                    it.copy(
                        isLoading = false,
                        plotSize = plotSize,
                        quantityNeeded = calculateRecommendedBags(plotSize) // Set initial quantity
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load farmer data"
                    )
                }
            }
        }
    }

    fun setSeason(season: String) {
        _state.update { it.copy(selectedSeason = season) }
    }

    fun setQuantity(quantity: Int) {
        _state.update { it.copy(quantityNeeded = quantity) }
    }

    fun submitApplication() {
        // Prevent multiple simultaneous submissions
        if (isSubmitting) return

        val currentState = _state.value

        if (currentState.selectedSeason.isNullOrEmpty()) {
            _state.update { it.copy(error = "Please select a season") }
            return
        }

        if (currentState.quantityNeeded <= 0) {
            _state.update { it.copy(error = "Please select a valid quantity") }
            return
        }

        isSubmitting = true
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                // TODO: Call repository to submit application
                kotlinx.coroutines.delay(1000)

                // Simulate submission
                val success = submitApplicationToRepository(
                    season = currentState.selectedSeason,
                    plotSize = currentState.plotSize,
                    quantityNeeded = currentState.quantityNeeded
                )

                if (success) {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            applicationSubmitted = true
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = "Submission failed - please try again"
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to submit application"
                    )
                }
            } finally {
                isSubmitting = false
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }

    private fun calculateRecommendedBags(plotSize: Double): Int {
        return (plotSize * 10).toInt().coerceAtLeast(1)
    }

    // TODO: Replace with actual repository calls
    private fun getPlotSizeFromProfile(): Double {
        // Get from PreferencesManager or UserRepository
        return 5.0
    }

    private suspend fun submitApplicationToRepository(
        season: String,
        plotSize: Double,
        quantityNeeded: Int
    ): Boolean {
        // TODO: Implement actual API call
        // This should send the application to the backend

        // Simulate API call with potential failure
        kotlinx.coroutines.delay(500) // Simulate network delay

        // Simulate 90% success rate for testing
        return Math.random() > 0.1
    }
}