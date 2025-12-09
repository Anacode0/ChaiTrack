package com.farmer.chaitrack.domain.usecase.finance

import com.farmer.chaitrack.domain.model.MonthlyTranscript
import com.farmer.chaitrack.domain.repository.FinanceRepository
import com.farmer.chaitrack.util.Result
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetTranscriptByPeriodUseCase @Inject constructor(
    private val repository: FinanceRepository
) {
    suspend operator fun invoke(startDate: LocalDate, endDate: LocalDate): Flow<Result<MonthlyTranscript>> {
        return repository.getTranscriptByPeriod(startDate, endDate)
    }
}