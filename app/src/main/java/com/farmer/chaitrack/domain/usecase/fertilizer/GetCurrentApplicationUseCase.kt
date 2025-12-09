package com.farmer.chaitrack.domain.usecase.fertilizer

import com.farmer.chaitrack.domain.model.FertilizerApplication
import com.farmer.chaitrack.domain.repository.FertilizerRepository
import com.farmer.chaitrack.util.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentApplicationUseCase @Inject constructor(
    private val repository: FertilizerRepository
) {
    suspend operator fun invoke(): Flow<Result<FertilizerApplication?>> {
        return repository.getCurrentApplication()
    }
}