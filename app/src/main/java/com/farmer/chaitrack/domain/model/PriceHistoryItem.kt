package com.farmer.chaitrack.domain.model

import java.time.LocalDate

data class PriceHistoryItem(
    val date: LocalDate,
    val pricePerKg: Double,
    val priceType: PriceType,
    val grade: String? = null,
    val collectionCenter: String? = null,
    val notes: String? = null
) {
    fun getFormattedDate(): String {
        return date.toString()
    }
}