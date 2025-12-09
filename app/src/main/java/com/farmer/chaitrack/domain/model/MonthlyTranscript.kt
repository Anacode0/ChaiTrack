package com.farmer.chaitrack.domain.model

data class MonthlyTranscript(
    val id: String? = null,
    val month: Int,
    val year: Int,
    val totalWeight: Double,
    val pricePerKg: Double,
    val totalEarnings: Double,
    val farmerId: String? = null,
    val collectionCenter: String? = null,
    val records: List<FinanceRecord>,
    val timestamp: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false
) {
    fun isValid(): Boolean {
        return month in 1..12 &&
                year >= 2020 &&
                totalWeight > 0 &&
                pricePerKg > 0 &&
                totalEarnings >= 0
    }

    fun calculateExpectedEarnings(): Double {
        return totalWeight * pricePerKg
    }

    fun hasMatchingEarnings(): Boolean {
        return totalEarnings == calculateExpectedEarnings()
    }

    fun getMonthName(): String {
        return when (month) {
            1 -> "January"
            2 -> "February"
            3 -> "March"
            4 -> "April"
            5 -> "May"
            6 -> "June"
            7 -> "July"
            8 -> "August"
            9 -> "September"
            10 -> "October"
            11 -> "November"
            12 -> "December"
            else -> "Unknown"
        }
    }

    fun getFormattedPeriod(): String {
        return "${getMonthName()} $year"
    }
}