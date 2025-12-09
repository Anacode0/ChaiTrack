package com.farmer.chaitrack.domain.usecase.finance

import com.farmer.chaitrack.domain.model.FinanceRecord
import javax.inject.Inject

class CalculateMonthlyEarningsUseCase @Inject constructor() {
    operator fun invoke(records: List<FinanceRecord>, pricePerKg: Double): Double {
        val totalWeight = records.sumOf { it.weight }
        return totalWeight * pricePerKg
    }
}