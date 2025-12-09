package com.farmer.chaitrack.data.remote.dto

import com.farmer.chaitrack.domain.model.FertilizerApplication
import com.farmer.chaitrack.domain.repository.FertilizerRepository
import com.farmer.chaitrack.util.Result
import javax.inject.Inject

class SyncApplicationsUseCase @Inject constructor(
    private val repository: FertilizerRepository
) {
    suspend operator fun invoke(): Result<List<FertilizerApplication>> {
        return repository.fetchAndSyncApplications()
    }
}