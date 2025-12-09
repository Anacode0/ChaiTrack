package com.farmer.chaitrack.domain.usecase.fertilizer

import com.farmer.chaitrack.domain.model.FertilizerApplication
import com.farmer.chaitrack.domain.repository.FertilizerRepository
import com.farmer.chaitrack.util.Result
import javax.inject.Inject

class CreateFertilizerApplicationUseCase @Inject constructor(
    private val fertilizerRepository: FertilizerRepository
) {
    suspend operator fun invoke(
        season: String,
        plotSize: Double,
        quantityNeeded: Int,
        fertilizerType: String,
        preferredPickupLocation: String? = null,
        notes: String? = null
    ): Result<FertilizerApplication> {
        return fertilizerRepository.submitApplication(
            season = season,
            plotSize = plotSize,
            quantityNeeded = quantityNeeded,
            fertilizerType = fertilizerType,
            preferredPickupLocation = preferredPickupLocation,
            notes = notes
        )
    }
}