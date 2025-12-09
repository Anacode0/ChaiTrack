package com.farmer.chaitrack.domain.usecase.finance

import com.farmer.chaitrack.domain.model.FinanceRecord
import com.farmer.chaitrack.domain.repository.FinanceRepository
import com.farmer.chaitrack.util.Result
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetEstimatedEarningsUseCase @Inject constructor(
    private val repository: FinanceRepository
) {
    suspend operator fun invoke(): Flow<Result<Double>> {
        return repository.getEstimatedEarnings()
    }
}