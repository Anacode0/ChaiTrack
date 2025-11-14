package com.farmer.chaitrack.ui.delivery

import java.util.Calendar

sealed class DeliveryFilter {
    data object ALL : DeliveryFilter()
    data object THIS_MONTH : DeliveryFilter()
    data object LAST_TWO_WEEKS : DeliveryFilter()
    data object LAST_MONTH : DeliveryFilter()
    data object LAST_THREE_MONTHS : DeliveryFilter()
    data object LAST_SIX_MONTHS : DeliveryFilter()
    data object LAST_YEAR : DeliveryFilter()

    /**
     * Calculates the start and end dates for this filter
     * @return Pair of (startTimestamp, endTimestamp)
     */
    fun getDateRange(): Pair<Long, Long> {
        val calendar = Calendar.getInstance()

        return when (this) {
            ALL -> {
                // For ALL, use a very wide date range (last 10 years to future)
                val startDate = calendar.apply {
                    add(Calendar.YEAR, -10)
                }.timeInMillis
                val endDate = calendar.apply {
                    add(Calendar.YEAR, 20) // Future date to include all
                }.timeInMillis
                startDate to endDate
            }

            THIS_MONTH -> {
                // Start of current month
                val startDate = calendar.apply {
                    set(Calendar.DAY_OF_MONTH, 1)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.timeInMillis
                // End of current month
                val endDate = calendar.apply {
                    set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
                    set(Calendar.HOUR_OF_DAY, 23)
                    set(Calendar.MINUTE, 59)
                    set(Calendar.SECOND, 59)
                    set(Calendar.MILLISECOND, 999)
                }.timeInMillis
                startDate to endDate
            }

            LAST_TWO_WEEKS -> {
                // End date is now
                val endDate = calendar.timeInMillis
                // Start date is 14 days ago
                val startDate = calendar.apply {
                    add(Calendar.DAY_OF_YEAR, -14)
                }.timeInMillis
                startDate to endDate
            }

            LAST_MONTH -> {
                // Go to previous month
                calendar.add(Calendar.MONTH, -1)
                // Start of previous month
                val startDate = calendar.apply {
                    set(Calendar.DAY_OF_MONTH, 1)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.timeInMillis
                // End of previous month
                val endDate = calendar.apply {
                    set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
                    set(Calendar.HOUR_OF_DAY, 23)
                    set(Calendar.MINUTE, 59)
                    set(Calendar.SECOND, 59)
                    set(Calendar.MILLISECOND, 999)
                }.timeInMillis
                startDate to endDate
            }

            LAST_THREE_MONTHS -> {
                // End date is now
                val endDate = calendar.timeInMillis
                // Start date is 3 months ago
                val startDate = calendar.apply {
                    add(Calendar.MONTH, -3)
                }.timeInMillis
                startDate to endDate
            }

            LAST_SIX_MONTHS -> {
                // End date is now
                val endDate = calendar.timeInMillis
                // Start date is 6 months ago
                val startDate = calendar.apply {
                    add(Calendar.MONTH, -6)
                }.timeInMillis
                startDate to endDate
            }

            LAST_YEAR -> {
                // End date is now
                val endDate = calendar.timeInMillis
                // Start date is 1 year ago
                val startDate = calendar.apply {
                    add(Calendar.YEAR, -1)
                }.timeInMillis
                startDate to endDate
            }
        }
    }

    companion object {
        fun values(): Array<DeliveryFilter> {
            return arrayOf(
                ALL,
                THIS_MONTH,
                LAST_TWO_WEEKS,
                LAST_MONTH,
                LAST_THREE_MONTHS,
                LAST_SIX_MONTHS,
                LAST_YEAR
            )
        }

        fun valueOf(value: String): DeliveryFilter {
            return when (value) {
                "ALL" -> ALL
                "THIS_MONTH" -> THIS_MONTH
                "LAST_TWO_WEEKS" -> LAST_TWO_WEEKS
                "LAST_MONTH" -> LAST_MONTH
                "LAST_THREE_MONTHS" -> LAST_THREE_MONTHS
                "LAST_SIX_MONTHS" -> LAST_SIX_MONTHS
                "LAST_YEAR" -> LAST_YEAR
                else -> throw IllegalArgumentException("No object com.farmer.chaitrack.ui.delivery.DeliveryFilter.$value")
            }
        }
    }
}