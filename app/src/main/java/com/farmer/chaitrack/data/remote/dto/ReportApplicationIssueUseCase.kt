package com.farmer.chaitrack.data.remote.dto

import com.farmer.chaitrack.domain.repository.FertilizerRepository
import com.farmer.chaitrack.util.Result
import javax.inject.Inject


class ReportApplicationIssueUseCase @Inject constructor(
    private val repository: FertilizerRepository
) {
    suspend operator fun invoke(
        applicationId: String,
        description: String,
        issueType: String
    ): Result<Boolean> {
        if (applicationId.isBlank()) {
            return Result.error("Application ID cannot be empty")
        }

        if (description.isBlank()) {
            return Result.error("Issue description cannot be empty")
        }

        return repository.reportApplicationIssue(
            applicationId = applicationId,
            issueType,
            description)
    }
}