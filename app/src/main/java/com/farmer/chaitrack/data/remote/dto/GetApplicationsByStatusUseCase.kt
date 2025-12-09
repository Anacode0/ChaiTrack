package com.farmer.chaitrack.data.remote.dto

import com.farmer.chaitrack.domain.model.FertilizerApplication
import com.farmer.chaitrack.domain.repository.FertilizerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetApplicationsByStatusUseCase @Inject constructor(
    private val repository: FertilizerRepository
) {
    operator fun invoke(status: String): Flow<List<FertilizerApplication>> {
        return repository.getApplicationsByStatus(status)
    }
}