package com.farmer.chaitrack.data.remote.dto

import com.farmer.chaitrack.domain.repository.FertilizerRepository
import javax.inject.Inject

class GetTotalApprovedQuantityUseCase @Inject constructor(
    private val repository: FertilizerRepository
) {
    suspend operator fun invoke(): Int {
        return repository.getTotalApprovedQuantity()
    }
}