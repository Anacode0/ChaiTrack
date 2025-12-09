package com.farmer.chaitrack.data.remote.dto

import com.farmer.chaitrack.domain.model.Month
import com.farmer.chaitrack.data.repository.FinanceRepositoryImpl
import com.farmer.chaitrack.util.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to get available months for which financial data exists
 *
 * This use case retrieves the list of months that have financial records,
 * typically used for filtering or displaying available data periods.
 */
class GetAvailableMonthsUseCase @Inject constructor(
    private val financeRepository: FinanceRepositoryImpl
) {
    suspend operator fun invoke(): Result<List<MonthDto>> {
        return financeRepository.getAvailableMonths()
    }

    fun invokeAsFlow(): Flow<Result<List<MonthDto>>> {
        return financeRepository.getAvailableMonthsFlow()
    }
}