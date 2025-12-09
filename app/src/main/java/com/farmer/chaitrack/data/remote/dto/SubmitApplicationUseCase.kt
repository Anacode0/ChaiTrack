package com.farmer.chaitrack.data.remote.dto

import com.farmer.chaitrack.domain.model.FertilizerApplication
import com.farmer.chaitrack.domain.repository.FertilizerRepository
import com.farmer.chaitrack.util.Result
import javax.inject.Inject

class SubmitApplicationUseCase @Inject constructor(
    private val repository: FertilizerRepository
) {
    suspend operator fun invoke(
        season: String,
        plotSize: Double,
        quantityNeeded: Int,
        fertilizerType: String
    ): Result<FertilizerApplication> {
        if (season.isBlank()) {
            return Result.Error("Season cannot be empty")
        }

        if (plotSize <= 0) {
            return Result.Error("Plot size must be greater than 0")
        }

        if (quantityNeeded <= 0) {
            return Result.Error("Quantity must be greater than 0")
        }

        if (fertilizerType.isBlank()) {
            return Result.Error("Fertilizer type cannot be empty")
        }

        return repository.submitApplication(
            season = season,
            plotSize = plotSize,
            quantityNeeded = quantityNeeded,
            fertilizerType = fertilizerType
        )
    }
}