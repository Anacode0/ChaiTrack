package com.farmer.chaitrack.domain.usecase.finance

import com.farmer.chaitrack.domain.model.FinanceRecord
import javax.inject.Inject

class GetTotalWeightForPeriodUseCase @Inject constructor() {
    operator fun invoke(records: List<FinanceRecord>, startDate: Long, endDate: Long): Double {
        return records
            .filter { it.date in startDate..endDate }
            .sumOf { it.weight }
    }
}