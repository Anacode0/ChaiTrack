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
                kotlinx.coroutines.delay(500)

                val userName = getUserName()

                val monthDeliveries = getMonthDeliveries()
                val todayWeight = getTodayWeight()

                val pricePerKg = getCurrentPrice()
                val deliveriesUpToToday = getDeliveriesUpToToday()
                val estimatedEarnings = deliveriesUpToToday * pricePerKg

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
                _state.update {
                    it.copy(unreadNotifications = 0)
                }
            } catch (e: Exception) {
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
        }
    }

    private fun getUserName(): String {
        return "Ronald Kipngetich"
    }

    private fun getMonthDeliveries(): Double {
        return 1250.50
    }

    private fun getTodayWeight(): Double {
        return 45.0
    }

    private fun getCurrentPrice(): Double {
        return 50.0
    }

    private fun getDeliveriesUpToToday(): Double {
        return 1250.50
    }

    private fun getUnreadNotificationCount(): Int {
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
