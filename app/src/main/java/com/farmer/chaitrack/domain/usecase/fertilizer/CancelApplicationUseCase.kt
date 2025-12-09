package com.farmer.chaitrack.domain.usecase.fertilizer

import com.farmer.chaitrack.domain.repository.FertilizerRepository
import com.farmer.chaitrack.util.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CancelApplicationUseCase @Inject constructor(
    private val repository: FertilizerRepository
) {
    suspend operator fun invoke(applicationId: String): Flow<Result<Boolean>> {
        return repository.cancelFertilizerApplication(applicationId)
    }
}