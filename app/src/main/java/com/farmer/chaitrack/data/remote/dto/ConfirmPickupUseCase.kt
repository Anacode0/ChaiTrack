package com.farmer.chaitrack.data.remote.dto

import com.farmer.chaitrack.domain.repository.FertilizerRepository
import com.farmer.chaitrack.util.Result
import javax.inject.Inject


class ConfirmPickupUseCase @Inject constructor(
    private val repository: FertilizerRepository
) {
    suspend operator fun invoke(
        applicationId: String,
        receivedQuantity: Int,
        conditionRating: Int
    ): Result<Boolean> {
        if (applicationId.isBlank()) {
            return Result.error("Application ID cannot be empty")
        }

        return repository.confirmPickup(
            applicationId,
            receivedQuantity,
            conditionRating)
    }
}