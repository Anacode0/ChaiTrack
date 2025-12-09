package com.farmer.chaitrack.data.remote.dto

import com.farmer.chaitrack.domain.model.FinancialMonth
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Locale

/**
 * Data transfer object representing a month with financial data
 */
@Parcelize
data class MonthDto(
    val year: Int,
    val monthNumber: Int,
    val monthName: String,
    val hasData: Boolean = false,
    val totalEarnings: Double = 0.0,
    val totalDeliveries: Int = 0,
    val isCurrent: Boolean = false
) : Parcelable {

    companion object {
        /**
         * Creates a Month object from year and month number
         */
        fun from(year: Int, monthNumber: Int): MonthDto {
            val monthNames = arrayOf(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
            )
            return MonthDto(
                year = year,
                monthNumber = monthNumber,
                monthName = monthNames.getOrNull(monthNumber - 1) ?: "Unknown"
            )
        }

        /**
         * Gets the current month
         */
        fun current(): MonthDto {
            val calendar = java.util.Calendar.getInstance()
            return from(
                calendar.get(java.util.Calendar.YEAR),
                calendar.get(java.util.Calendar.MONTH) + 1
            )
        }
    }

    /**
     * Returns the display name (e.g., "January 2024")
     */
    fun displayName(): String = "$monthName $year"

    /**
     * Returns the short display name (e.g., "Jan 2024")
     */
    fun shortDisplayName(): String {
        val shortMonthName = monthName.take(3)
        return "$shortMonthName $year"
    }

    /**
     * Returns the period identifier (e.g., "2024-01")
     */
    fun periodId(): String = String.format(Locale.getDefault(), "%04d-%02d", year, monthNumber)
}

/**
 * Convert Month DTO to FinancialMonth Domain Model
 */
fun MonthDto.toDomainModel(): FinancialMonth {
    return FinancialMonth(
        year = this.year,
        monthNumber = this.monthNumber,
        monthName = this.monthName,
        hasData = this.hasData,
        totalEarnings = this.totalEarnings,
        totalDeliveries = this.totalDeliveries,
        isCurrent = this.isCurrent
    )
}

/**
 * Convert FinancialMonth Domain Model to Month DTO
 */
fun FinancialMonth.toDto(): MonthDto {
    return MonthDto(
        year = this.year,
        monthNumber = this.monthNumber,
        monthName = this.monthName,
        hasData = this.hasData,
        totalEarnings = this.totalEarnings,
        totalDeliveries = this.totalDeliveries,
        isCurrent = this.isCurrent
    )
}