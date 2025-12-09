package com.farmer.chaitrack.domain.usecase.finance

import com.farmer.chaitrack.domain.model.MonthlyTranscript
import com.farmer.chaitrack.domain.repository.FinanceRepository
import com.farmer.chaitrack.util.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMonthlyTranscriptUseCase @Inject constructor(
    private val repository: FinanceRepository
) {
    suspend operator fun invoke(month: Int, year: Int): Flow<Result<MonthlyTranscript>> {
        return repository.getMonthlyTranscript(month, year)
    }
}