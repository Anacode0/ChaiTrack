package com.farmer.chaitrack.domain.usecase.finance

import com.farmer.chaitrack.domain.model.PriceHistory
import com.farmer.chaitrack.domain.repository.FinanceRepository
import com.farmer.chaitrack.util.Result
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetPriceHistoryUseCase @Inject constructor(
    private val repository: FinanceRepository
) {
    suspend operator fun invoke(
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<Result<List<PriceHistory>>> {
        return repository.getPriceHistory(startDate, endDate)
    }

    suspend operator fun invoke(): Flow<Result<List<PriceHistory>>> {
        val endDate = LocalDate.now()
        val startDate = endDate.minusDays(30)
        return repository.getPriceHistory(startDate, endDate)
    }
}