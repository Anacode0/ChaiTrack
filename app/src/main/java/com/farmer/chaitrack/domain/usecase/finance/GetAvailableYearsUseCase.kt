package com.farmer.chaitrack.domain.usecase.finance

import com.farmer.chaitrack.domain.repository.FinanceRepository
import com.farmer.chaitrack.util.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAvailableYearsUseCase @Inject constructor(
    private val repository: FinanceRepository
) {
    suspend operator fun invoke(): Flow<Result<List<Int>>> {
        return repository.getAvailableYears()
    }
}