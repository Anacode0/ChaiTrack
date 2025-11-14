package com.farmer.chaitrack.ui.fertilizer


data class NewApplicationState(
    val isLoading: Boolean = false,
    val selectedSeason: String? = null,
    val plotSize: Double = 0.0,
    val quantityNeeded: Int = 0,
    val applicationSubmitted: Boolean = false,
    val error: String? = null
)