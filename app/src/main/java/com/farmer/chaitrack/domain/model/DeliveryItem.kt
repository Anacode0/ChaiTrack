package com.farmer.chaitrack.domain.model

import java.util.Date

data class DeliveryItem(
    val id: String,
    val userId: String,
    val clerkName: String,
    val weight: Double,
    val date: Long,
    val time: String,
    val monthTotal: Double? = null,
    val status: String = "completed",
    val pricePerKg: Double? = null,
    val totalAmount: Double? = null,
    val collectionCenter: String? = null
)