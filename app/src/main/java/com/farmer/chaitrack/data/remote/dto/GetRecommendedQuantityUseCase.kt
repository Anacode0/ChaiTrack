package com.farmer.chaitrack.data.remote.dto

import com.farmer.chaitrack.domain.repository.FertilizerRepository
import com.farmer.chaitrack.util.Result
import javax.inject.Inject

class GetRecommendedQuantityUseCase @Inject constructor(
    private val repository: FertilizerRepository
) {
    suspend operator fun invoke(plotSize: Double): Result<Int> {
        if (plotSize <= 0) {
            return Result.error("Plot size must be greater than 0")
        }
        return repository.getRecommendedQuantity(plotSize)
    }
}