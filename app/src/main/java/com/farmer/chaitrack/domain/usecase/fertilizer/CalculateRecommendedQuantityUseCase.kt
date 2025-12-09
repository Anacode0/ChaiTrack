package com.farmer.chaitrack.domain.usecase.fertilizer

import javax.inject.Inject

class CalculateRecommendedQuantityUseCase @Inject constructor() {
    operator fun invoke(plotSizeInAcres: Double): Int {
        // Standard recommendation: 2 bags per acre for tea plantations
        val bagsPerAcre = 2.0
        return (plotSizeInAcres * bagsPerAcre).toInt().coerceAtLeast(1)
    }
}