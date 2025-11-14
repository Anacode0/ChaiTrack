package com.farmer.chaitrack.ui.delivery

import com.farmer.chaitrack.domain.model.DeliveryItem

data class DeliveryState(
    val isLoading: Boolean = false,
    val deliveries: List<DeliveryItem> = emptyList(),
    val currentFilter: DeliveryFilter? = null,
    val selectedYear: Int? = null,
    val totalWeight: Double = 0.0,
    val issueReported: Boolean = false,
    val error: String? = null
)