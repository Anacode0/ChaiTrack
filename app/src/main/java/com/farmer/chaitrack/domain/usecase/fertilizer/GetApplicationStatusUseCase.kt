package com.farmer.chaitrack.domain.usecase.fertilizer

import com.farmer.chaitrack.domain.model.FertilizerApplication
import com.farmer.chaitrack.domain.repository.FertilizerRepository
import com.farmer.chaitrack.util.Result
import javax.inject.Inject

//Use case to get the status of a specific fertilizer application
class GetApplicationStatusUseCase @Inject constructor(
    private val repository: FertilizerRepository
) {

    /**
     * Get application by ID
     * @param applicationId The ID of the application to retrieve
     * @return Result containing the application details or error
     */
    suspend operator fun invoke(applicationId: String): Result<FertilizerApplication> {
        return try {
            // First try to get from local database
            val application = repository.getApplicationById(applicationId)

            if (application != null) {
                Result.success(application)
            } else {
                // If not found locally, try to sync from server
                when (val syncResult = repository.fetchAndSyncApplications()) {
                    is Result.Success -> {
                        // After sync, try to get the application again
                        val syncedApplication = repository.getApplicationById(applicationId)
                        if (syncedApplication != null) {
                            Result.success(syncedApplication)
                        } else {
                            Result.error("Application not found")
                        }
                    }
                    is Result.Error -> {
                        Result.error(
                            message = syncResult.message ?: "An error occurred",
                            throwable = syncResult.throwable ?: Exception(syncResult.message ?: "Unknown error")
                        )
                    }
                    is Result.Loading -> {
                        // This shouldn't happen for a suspend function, but handle it anyway
                        Result.error("Sync in progress, please try again")
                    }
                }
            }
        } catch (e: Exception) {
            Result.error(
                message = e.message ?: "Failed to get application status",
                throwable = e
            )
        }
    }
}