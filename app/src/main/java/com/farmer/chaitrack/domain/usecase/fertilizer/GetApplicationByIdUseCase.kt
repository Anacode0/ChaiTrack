package com.farmer.chaitrack.domain.usecase.fertilizer

import com.farmer.chaitrack.domain.model.FertilizerApplication
import com.farmer.chaitrack.domain.repository.FertilizerRepository
import com.farmer.chaitrack.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetApplicationByIdUseCase @Inject constructor(
    private val repository: FertilizerRepository
) {
    suspend operator fun invoke(applicationId: String): Flow<Result<FertilizerApplication>> {
        return flow {
            emit(Result.Loading)
            val application = repository.getApplicationById(applicationId)
            if (application != null) {
                emit(Result.Success(application))
            } else {
                emit(Result.Error("Application not found"))
            }
        }
    }
}