package com.farmer.chaitrack.domain.usecase.finance

import com.farmer.chaitrack.domain.repository.FinanceRepository
import com.farmer.chaitrack.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAvailableMonthsUseCase @Inject constructor(
    private val repository: FinanceRepository
) {
    operator fun invoke(year: Int): Flow<Result<List<Int>>> = flow {
        emit(Result.Loading)
        try {
            val result = repository.getAvailableMonths(year)
            when (result) {
                is Result.Success -> {
                    val monthNumbers = result.data.map { it.monthNumber }
                    emit(Result.Success(monthNumbers))
                }
                is Result.Error -> emit(Result.Error(result.message))
                else -> emit(Result.Error("Unknown result type"))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Failed to get available months"))
        }
    }
}