package com.farmer.chaitrack.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar

class MainViewModel : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    fun loadDashboardData() {
        viewModelScope.launch {
            try {
                // TODO: Replace with actual repository calls
                // Simulate loading data
                kotlinx.coroutines.delay(500)

                // Get user info
                val userName = getUserName()

                // Get deliveries data
                val monthDeliveries = getMonthDeliveries()
                val todayWeight = getTodayWeight()

                // Get price and calculate earnings
                val pricePerKg = getCurrentPrice()
                val deliveriesUpToToday = getDeliveriesUpToToday()
                val estimatedEarnings = deliveriesUpToToday * pricePerKg

                // Get notification count
                val unreadNotifications = getUnreadNotificationCount()

                _state.update {
                    it.copy(
                        userName = userName,
                        monthDeliveries = monthDeliveries,
                        todayWeight = todayWeight,
                        pricePerKg = pricePerKg,
                        estimatedEarnings = estimatedEarnings,
                        unreadNotifications = unreadNotifications,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = e.message,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun markNotificationsAsRead() {
        viewModelScope.launch {
            try {
                // TODO: Call repository to mark notifications as read
                _state.update {
                    it.copy(unreadNotifications = 0)
                }
            } catch (e: Exception) {
                // Handle error silently or show toast
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            // TODO: Clear user session from preferences
            // TODO: Clear any cached data
        }
    }

    // TODO: Replace these with actual repository calls
    private fun getUserName(): String {
        // TODO: Get from PreferencesManager or UserRepository
        return "Ronald Kipngetich"
    }

    private fun getMonthDeliveries(): Double {
        // TODO: Get from DeliveryRepository
        // Calculate total deliveries for current month
        return 1250.50
    }

    private fun getTodayWeight(): Double {
        // TODO: Get from DeliveryRepository
        // Get deliveries for today
        return 45.0
    }

    private fun getCurrentPrice(): Double {
        // TODO: Get from FinanceRepository or API
        // This should be the current buying price per kg
        return 50.0
    }

    private fun getDeliveriesUpToToday(): Double {
        // TODO: Get from DeliveryRepository
        // Calculate deliveries from start of month to today
        // This is used for earnings calculation
        return 1250.50
    }

    private fun getUnreadNotificationCount(): Int {
        // TODO: Get from NotificationRepository
        return 5
    }

    private fun getCurrentMonthName(): String {
        val calendar = Calendar.getInstance()
        val monthNames = arrayOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )
        return monthNames[calendar.get(Calendar.MONTH)]
    }
}