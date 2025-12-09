package com.farmer.chaitrack.domain.usecase.fertilizer

import com.farmer.chaitrack.domain.model.FertilizerApplication
import com.farmer.chaitrack.domain.repository.FertilizerRepository
import com.farmer.chaitrack.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class GetApplicationHistoryUseCase @Inject constructor(
    private val repository: FertilizerRepository
) {
    operator fun invoke(): Flow<Result<List<FertilizerApplication>>> {
        return repository.getApplicationHistory()
            .map { applications ->
                Result.Success(data = applications) as Result<List<FertilizerApplication>>
            }
            .catch { e ->
                emit(Result.Error(
                    message = e.message ?: "Failed to load application history",
                    throwable = e
                ))
            }
    }
}