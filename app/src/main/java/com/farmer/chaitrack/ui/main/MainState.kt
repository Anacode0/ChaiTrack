package com.farmer.chaitrack.ui.main

data class MainState(
    val isLoading: Boolean = false,
    val userName: String = "",
    val monthDeliveries: Double = 0.0,
    val todayWeight: Double = 0.0,
    val pricePerKg: Double = 0.0,
    val estimatedEarnings: Double = 0.0,
    val unreadNotifications: Int = 0,
    val error: String? = null
)