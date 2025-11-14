package com.farmer.chaitrack.ui.fertilizer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmer.chaitrack.domain.model.FertilizerApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class FertilizerViewModel : ViewModel() {

    private val _state = MutableStateFlow(FertilizerState())
    val state: StateFlow<FertilizerState> = _state.asStateFlow()

    fun loadDashboardData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                // TODO: Replace with actual repository calls
                kotlinx.coroutines.delay(800)

                val currentApp = getCurrentApplication()
                val history = getApplicationHistory()
                val canApply = checkIfCanApply()

                _state.update {
                    it.copy(
                        isLoading = false,
                        currentApplication = currentApp,
                        applicationHistory = history,
                        canApply = canApply
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load fertilizer data"
                    )
                }
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }

    // TODO: Replace with actual repository calls
    private fun getCurrentApplication(): FertilizerApplication? {
        // Sample data - replace with actual repository call
        return FertilizerApplication(
            id = "1",
            season = "Spring 2024",
            plotSize = 2.5, // Added plotSize
            quantityRequested = 50,
            quantityApproved = 50,
            recommendedQuantity = 45, // Added recommendedQuantity
            status = ApplicationStatus.APPROVED,
            applicationDate = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7), // Added applicationDate
            approvalDate = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(5),
            approvedBy = "Agricultural Officer",
            approverId = "AO123",
            pickupLocation = "Main Distribution Center"
        )
    }

    private fun getApplicationHistory(): List<FertilizerApplication> {
        // Sample data - replace with actual repository call
        val currentTime = System.currentTimeMillis()
        return listOf(
            FertilizerApplication(
                id = "2",
                season = "Fall 2023",
                plotSize = 2.3,
                quantityRequested = 45,
                quantityApproved = 45,
                recommendedQuantity = 42,
                status = ApplicationStatus.COMPLETED,
                applicationDate = currentTime - TimeUnit.DAYS.toMillis(180),
                approvalDate = currentTime - TimeUnit.DAYS.toMillis(175),
                pickupDate = currentTime - TimeUnit.DAYS.toMillis(170),
                completionDate = currentTime - TimeUnit.DAYS.toMillis(165),
                approvedBy = "Agricultural Officer",
                approverId = "AO123",
                pickupLocation = "North Distribution Center"
            ),
            FertilizerApplication(
                id = "3",
                season = "Spring 2023",
                plotSize = 2.0,
                quantityRequested = 50,
                quantityApproved = 40,
                recommendedQuantity = 38,
                status = ApplicationStatus.COMPLETED,
                applicationDate = currentTime - TimeUnit.DAYS.toMillis(360),
                approvalDate = currentTime - TimeUnit.DAYS.toMillis(355),
                pickupDate = currentTime - TimeUnit.DAYS.toMillis(350),
                completionDate = currentTime - TimeUnit.DAYS.toMillis(345),
                approvedBy = "Agricultural Officer",
                approverId = "AO123",
                pickupLocation = "South Distribution Center",
                notes = "Reduced quantity due to soil test results"
            )
        )
    }

    private fun checkIfCanApply(): Boolean {
        // TODO: Check with repository if applications are currently open
        // This should depend on the current season and application periods
        return true
    }
}