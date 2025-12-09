package com.farmer.chaitrack.domain.usecase.fertilizer

import com.farmer.chaitrack.domain.repository.FertilizerRepository
import com.farmer.chaitrack.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAvailableSeasonsUseCase @Inject constructor(
    private val repository: FertilizerRepository
) {
    suspend operator fun invoke(): Flow<List<String>> {
        return flow {
            val result = repository.getAvailableSeasons()
            when (result) {
                is Result.Success -> emit(result.data)
                is Result.Error -> emit(emptyList()) // Return empty list on error
                is Result.Loading -> emit(emptyList()) // Return empty list while loading
            }
        }
    }
}