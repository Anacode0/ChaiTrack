package com.farmer.chaitrack.domain.usecase.finance

import com.farmer.chaitrack.domain.model.PaymentRecord
import com.farmer.chaitrack.domain.repository.FinanceRepository
import com.farmer.chaitrack.util.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPaymentHistoryUseCase @Inject constructor(
    private val repository: FinanceRepository
) {
    suspend operator fun invoke(): Flow<Result<List<PaymentRecord>>> {
        return repository.getPaymentHistory()
    }
}