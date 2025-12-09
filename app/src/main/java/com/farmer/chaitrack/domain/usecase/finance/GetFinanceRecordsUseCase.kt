package com.farmer.chaitrack.domain.usecase.finance

import com.farmer.chaitrack.domain.model.FinanceRecord
import com.farmer.chaitrack.domain.repository.FinanceRepository
import com.farmer.chaitrack.util.Result
import com.farmer.chaitrack.domain.model.MonthlyTranscript
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFinanceRecordsUseCase @Inject constructor(
    private val repository: FinanceRepository
) {
    suspend operator fun invoke(): Flow<Result<List<FinanceRecord>>> {
        return repository.getFinanceRecords()
    }
}