package com.farmer.chaitrack.domain.usecase.fertilizer

import com.farmer.chaitrack.domain.repository.FertilizerRepository
import com.farmer.chaitrack.util.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckApplicationEligibilityUseCase @Inject constructor(
    private val repository: FertilizerRepository
) {

    data class EligibilityResult(
        val isEligible: Boolean,
        val reason: String? = null
    )

    suspend operator fun invoke(season: String): Flow<Result<EligibilityResult>> {
        return repository.checkApplicationEligibility(season)
    }
}