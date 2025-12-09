package com.farmer.chaitrack.domain.model

import java.util.Locale

data class FinanceRecord(
    val id: String,
    val month: Int,
    val date: Long,
    val weight: Double,
    val amount: Double,
    val type: String,
    val year: Int,
    val totalWeight: Double,
    val pricePerKg: Double,
    val totalEarnings: Double,
    val deductions: Double,
    val netEarnings: Double,
    val generatedDate: Long,
    val pdfUrl: String?,
    val transactionReference: String?,
    val pdfGenerated: Boolean = false,
    val pdfFilePath: String? = null,
    val deliveryCount: Int = 0,
    val paymentStatus: String = "PENDING"

) {

    fun getMonthDisplayText(): String {
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
    fun getTranscriptTitle(): String {
        return "Transcript for ${getMonthDisplayText()} $year"
    }
    fun getFormattedTotalWeight(): String {
        return "${"%.1f".format(totalWeight)} kg"
    }
    fun getFormattedPricePerKg(): String {
        return "KES ${"%.2f".format(pricePerKg)}"
    }
    fun getFormattedTotalEarnings(): String {
        return "KES ${"%,.2f".format(totalEarnings)}"
    }
    fun getFormattedDeductions(): String {
        return "KES ${"%,.2f".format(deductions)}"
    }
    fun getFormattedNetEarnings(): String {
        return "KES ${"%,.2f".format(netEarnings)}"
    }
    fun hasPdf(): Boolean {
        return !pdfUrl.isNullOrEmpty()
    }
    fun getPeriodIdentifier(): String {
        return "$year-${month.toString().padStart(2, '0')}"
    }
    fun isCurrentPeriod(currentMonth: Int, currentYear: Int): Boolean {
        return month == currentMonth && year == currentYear
    }
}

data class FinanceSummary(
    val monthToDateWeight: Double,
    val monthToDateEarnings: Double,
    val todayWeight: Double,
    val averageWeightPerDay: Double,
    val projectedMonthEarnings: Double,
    val currentPricePerKg: Double
) {
    fun getFormattedMonthToDateWeight(): String {
        return "${"%.1f".format(monthToDateWeight)} kg"
    }
    fun getFormattedMonthToDateEarnings(): String {
        return "KES ${"%,.2f".format(monthToDateEarnings)}"
    }
    fun getFormattedTodayWeight(): String {
        return "${"%.1f".format(todayWeight)} kg"
    }
    fun getFormattedProjectedEarnings(): String {
        return "KES ${"%,.2f".format(projectedMonthEarnings)}"
    }
    fun getFormattedCurrentPrice(): String {
        return "KES ${"%.2f".format(currentPricePerKg)}"
    }
}
enum class Month(val number: Int, val displayName: String) {
    JANUARY(1, "January"),
    FEBRUARY(2, "February"),
    MARCH(3, "March"),
    APRIL(4, "April"),
    MAY(5, "May"),
    JUNE(6, "June"),
    JULY(7, "July"),
    AUGUST(8, "August"),
    SEPTEMBER(9, "September"),
    OCTOBER(10, "October"),
    NOVEMBER(11, "November"),
    DECEMBER(12, "December");

    companion object {
        fun fromNumber(number: Int): Month {
            return values().find { it.number == number } ?: JANUARY
        }
        fun displayName(monthNumber: Int, year: Int): String {
            val month = fromNumber(monthNumber)
            return "${month.displayName} $year"
        }
        fun periodId(monthNumber: Int, year: Int): String {
            return String.format(Locale.getDefault(), "%04d-%02d", year, monthNumber)
        }

        fun getAllMonths(): List<Month> {
            return values().toList()
        }
        fun current(): Month {
            val calendar = java.util.Calendar.getInstance()
            val currentMonth = calendar.get(java.util.Calendar.MONTH) + 1
            return fromNumber(currentMonth)
        }
        fun currentYear(): Int {
            val calendar = java.util.Calendar.getInstance()
            return calendar.get(java.util.Calendar.YEAR)
        }
    }
}
data class FinancialMonth(
    val year: Int,
    val monthNumber: Int,
    val monthName: String,
    val hasData: Boolean = false,
    val totalEarnings: Double = 0.0,
    val totalDeliveries: Int = 0,
    val isCurrent: Boolean = false
) {
    fun displayName(): String = "$monthName $year"

    fun periodId(): String = "$year-${monthNumber.toString().padStart(2, '0')}"

    fun getMonthEnum(): Month {
        return Month.fromNumber(monthNumber)
    }

    fun isCurrentMonth(): Boolean {
        val currentMonth = Month.current()
        val currentYear = Month.currentYear()
        return monthNumber == currentMonth.number && year == currentYear
    }
}