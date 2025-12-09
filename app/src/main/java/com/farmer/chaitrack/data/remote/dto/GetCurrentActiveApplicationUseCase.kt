package com.farmer.chaitrack.data.remote.dto

import com.farmer.chaitrack.domain.model.FertilizerApplication
import com.farmer.chaitrack.domain.repository.FertilizerRepository
import javax.inject.Inject


class GetCurrentActiveApplicationUseCase @Inject constructor(
    private val repository: FertilizerRepository
) {
    suspend operator fun invoke(): FertilizerApplication? {
        return repository.getCurrentActiveApplication()
    }
}