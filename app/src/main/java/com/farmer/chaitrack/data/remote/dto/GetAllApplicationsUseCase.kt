package com.farmer.chaitrack.data.remote.dto

import com.farmer.chaitrack.domain.model.FertilizerApplication
import com.farmer.chaitrack.domain.repository.FertilizerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllApplicationsUseCase @Inject constructor(
    private val repository: FertilizerRepository
) {
    operator fun invoke(): Flow<List<FertilizerApplication>> {
        return repository.getApplicationsFlow()
    }
}