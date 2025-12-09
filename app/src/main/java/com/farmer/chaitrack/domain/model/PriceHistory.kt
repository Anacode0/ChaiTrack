package com.farmer.chaitrack.domain.model

import org.threeten.bp.LocalDate

data class PriceHistory(
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

enum class PriceType {
    CURRENT,
    HISTORICAL,
    PROJECTED,
    SEASONAL_AVERAGE
}