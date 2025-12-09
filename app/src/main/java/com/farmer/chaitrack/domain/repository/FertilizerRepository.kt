package com.farmer.chaitrack.domain.repository

import com.farmer.chaitrack.domain.model.FertilizerApplication
import com.farmer.chaitrack.data.remote.dto.FertilizerStatisticsData
import com.farmer.chaitrack.domain.usecase.fertilizer.CheckApplicationEligibilityUseCase
import com.farmer.chaitrack.util.Result
import kotlinx.coroutines.flow.Flow

interface FertilizerRepository {
    fun getApplicationsFlow(): Flow<List<FertilizerApplication>>
    fun getApplicationsByStatus(status: String): Flow<List<FertilizerApplication>>
    fun getApplicationHistory(): Flow<List<FertilizerApplication>>
    suspend fun getCurrentActiveApplication(): FertilizerApplication?
    suspend fun getFertilizerApplications(): Flow<Result<List<FertilizerApplication>>>
    suspend fun getCurrentApplication(): Flow<Result<FertilizerApplication>>
    suspend fun getApprovedQuantity(applicationId: String): Flow<Result<Int>>
    suspend fun markAsPickedUp(applicationId: String): Flow<Result<Boolean>>
    suspend fun updateFertilizerApplication(application: FertilizerApplication): Result<FertilizerApplication>
    suspend fun getApplicationById(applicationId: String): FertilizerApplication?
    suspend fun fetchAndSyncApplications(): Result<List<FertilizerApplication>>
    suspend fun submitApplication(
        season: String,
        plotSize: Double,
        quantityNeeded: Int,
        fertilizerType: String,
        preferredPickupLocation: String? = null,
        notes: String? = null
    ): Result<FertilizerApplication>
    suspend fun areApplicationsOpen(): Result<Boolean>
    suspend fun getApplicationByIdFlow(applicationId: String): Flow<Result<FertilizerApplication>>
    suspend fun cancelFertilizerApplication(applicationId: String): Flow<Result<Boolean>>
    suspend fun getAvailableSeasons(): Result<List<String>>
    suspend fun getRecommendedQuantity(plotSize: Double): Result<Int>
    suspend fun getApplicationStatistics(): Result<FertilizerStatisticsData>
    suspend fun confirmPickup(
        applicationId: String,
        receivedQuantity: Int,
        conditionRating: Int,
        feedback: String? = null
    ): Result<Boolean>
    suspend fun reportApplicationIssue(
        applicationId: String,
        issueType: String,
        description: String,
        suggestedResolution: String? = null,
        attachments: List<String>? = null
    ): Result<Boolean>
    suspend fun getPendingApplicationCount(): Int
    suspend fun getTotalApprovedQuantity(): Int
    fun checkApplicationEligibility(season: String): kotlinx.coroutines.flow.Flow<com.farmer.chaitrack.util.Result<com.farmer.chaitrack.domain.usecase.fertilizer.CheckApplicationEligibilityUseCase.EligibilityResult>>
}