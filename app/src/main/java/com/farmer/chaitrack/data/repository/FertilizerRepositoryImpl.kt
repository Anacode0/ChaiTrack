package com.farmer.chaitrack.data.repository

import com.farmer.chaitrack.data.local.database.dao.FertilizerDao
import com.farmer.chaitrack.data.local.entity.FertilizerEntity
import com.farmer.chaitrack.data.local.preferences.PreferencesManager
import com.farmer.chaitrack.data.remote.api.FertilizerApi
import com.farmer.chaitrack.data.remote.dto.*
import com.farmer.chaitrack.domain.model.FertilizerApplication
import com.farmer.chaitrack.domain.repository.FertilizerRepository
import com.farmer.chaitrack.domain.usecase.fertilizer.CheckApplicationEligibilityUseCase
import com.farmer.chaitrack.ui.fertilizer.ApplicationStatus
import com.farmer.chaitrack.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FertilizerRepositoryImpl @Inject constructor(
    private val fertilizerApi: FertilizerApi,
    private val fertilizerDao: FertilizerDao,
    private val preferencesManager: PreferencesManager
) : FertilizerRepository {

    override fun getApplicationsFlow(): Flow<List<FertilizerApplication>> {
        return fertilizerDao.getAllApplications().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getApplicationsByStatus(status: String): Flow<List<FertilizerApplication>> {
        return fertilizerDao.getApplicationsByStatus(status).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getApplicationHistory(): Flow<List<FertilizerApplication>> {
        return fertilizerDao.getApplicationHistory().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun getCurrentActiveApplication(): FertilizerApplication? {
        return withContext(Dispatchers.IO) {
            fertilizerDao.getCurrentActiveApplication()?.toDomainModel()
        }
    }

    override suspend fun getFertilizerApplications(): Flow<Result<List<FertilizerApplication>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getCurrentApplication(): Flow<Result<FertilizerApplication>> {
        TODO("Not yet implemented")
    }

    override suspend fun getApprovedQuantity(applicationId: String): Flow<Result<Int>> {
        TODO("Not yet implemented")
    }

    override suspend fun markAsPickedUp(applicationId: String): Flow<Result<Boolean>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateFertilizerApplication(application: FertilizerApplication): Result<FertilizerApplication> {
        TODO("Not yet implemented")
    }

    override suspend fun getApplicationById(applicationId: String): FertilizerApplication? {
        return withContext(Dispatchers.IO) {
            fertilizerDao.getApplicationById(applicationId)?.toDomainModel()
        }
    }

    override suspend fun fetchAndSyncApplications(): Result<List<FertilizerApplication>> {
        return withContext(Dispatchers.IO) {
            try {
                val token = preferencesManager.getAuthToken()
                if (token == null) {
                    return@withContext Result.error<List<FertilizerApplication>>("Not authenticated")
                }

                val response = fertilizerApi.getApplications("Bearer $token")

                if (response.isSuccessful && response.body()?.success == true) {
                    val applications = response.body()?.data?.applications ?: emptyList()

                    val entities = applications.map { it.toEntity() }
                    fertilizerDao.insertApplications(entities)

                    preferencesManager.updateLastSyncTime()

                    Result.success(entities.map { it.toDomainModel() })
                } else {
                    Result.error<List<FertilizerApplication>>("Failed to fetch applications")
                }
            } catch (e: Exception) {
                Result.error<List<FertilizerApplication>>(e.message ?: "Network error occurred", e)
            }
        }
    }

    override suspend fun submitApplication(
        season: String,
        plotSize: Double,
        quantityNeeded: Int,
        fertilizerType: String,
        preferredPickupLocation: String?,
        notes: String?
    ): Result<FertilizerApplication> {
        return withContext(Dispatchers.IO) {
            try {
                val token = preferencesManager.getAuthToken()
                if (token == null) {
                    return@withContext Result.error<FertilizerApplication>("Not authenticated")
                }

                val request = SubmitFertilizerApplicationRequest(
                    season = season,
                    plotSize = plotSize,
                    quantityRequested = quantityNeeded,
                    fertilizerType = fertilizerType,
                    preferredPickupLocation = preferredPickupLocation,
                    notes = notes
                )

                val response = fertilizerApi.submitApplication("Bearer $token", request)

                if (response.isSuccessful && response.body()?.success == true) {
                    val applicationDto = response.body()?.data?.application
                    if (applicationDto != null) {
                        val entity = applicationDto.toEntity()
                        fertilizerDao.insertApplication(entity)

                        Result.success(entity.toDomainModel())
                    } else {
                        Result.error<FertilizerApplication>("Invalid response data")
                    }
                } else {
                    val errorMessage = response.body()?.message ?: "Failed to submit application"
                    Result.error<FertilizerApplication>(errorMessage)
                }
            } catch (e: Exception) {
                Result.error<FertilizerApplication>(e.message ?: "Network error occurred", e)
            }
        }
    }

    override suspend fun areApplicationsOpen(): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val token = preferencesManager.getAuthToken()
                if (token == null) {
                    return@withContext Result.error<Boolean>("Not authenticated")
                }

                val response = fertilizerApi.areApplicationsOpen("Bearer $token")

                if (response.isSuccessful && response.body()?.success == true) {
                    val isOpen = response.body()?.data?.isOpen ?: false
                    Result.success(isOpen)
                } else {
                    Result.error<Boolean>("Failed to check application status")
                }
            } catch (e: Exception) {
                Result.error<Boolean>(e.message ?: "Network error occurred", e)
            }
        }
    }

    override suspend fun getApplicationByIdFlow(applicationId: String): Flow<Result<FertilizerApplication>> {
        TODO("Not yet implemented")
    }

    override suspend fun cancelFertilizerApplication(applicationId: String): Flow<Result<Boolean>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAvailableSeasons(): Result<List<String>> {
        return withContext(Dispatchers.IO) {
            try {
                val token = preferencesManager.getAuthToken()
                if (token == null) {
                    return@withContext Result.error<List<String>>("Not authenticated")
                }

                val response = fertilizerApi.getAvailableSeasons("Bearer $token")

                if (response.isSuccessful && response.body()?.success == true) {
                    val seasons = response.body()?.data?.seasons ?: emptyList()
                    Result.success(seasons)
                } else {
                    Result.error<List<String>>("Failed to fetch available seasons")
                }
            } catch (e: Exception) {
                Result.error<List<String>>(e.message ?: "Network error occurred", e)
            }
        }
    }

    override suspend fun getRecommendedQuantity(plotSize: Double): Result<Int> {
        return withContext(Dispatchers.IO) {
            try {
                val token = preferencesManager.getAuthToken()
                if (token == null) {
                    return@withContext Result.error<Int>("Not authenticated")
                }

                val response = fertilizerApi.getRecommendedQuantity("Bearer $token", plotSize)

                if (response.isSuccessful && response.body()?.success == true) {
                    val quantity = response.body()?.data?.recommendedQuantity
                    if (quantity != null) {
                        Result.success(quantity)
                    } else {
                        Result.success(calculateRecommendedQuantity(plotSize))
                    }
                } else {
                    Result.success(calculateRecommendedQuantity(plotSize))
                }
            } catch (e: Exception) {
                Result.success(calculateRecommendedQuantity(plotSize))
            }
        }
    }

    override suspend fun getApplicationStatistics(): Result<FertilizerStatisticsData> {
        return withContext(Dispatchers.IO) {
            try {
                val token = preferencesManager.getAuthToken()
                if (token == null) {
                    return@withContext Result.error<FertilizerStatisticsData>("Not authenticated")
                }

                val response = fertilizerApi.getApplicationStatistics("Bearer $token")

                if (response.isSuccessful && response.body()?.success == true) {
                    val stats = response.body()?.data
                    if (stats != null) {
                        Result.success(stats)
                    } else {
                        Result.error<FertilizerStatisticsData>("Invalid statistics data")
                    }
                } else {
                    Result.error<FertilizerStatisticsData>("Failed to fetch statistics")
                }
            } catch (e: Exception) {
                Result.error<FertilizerStatisticsData>(e.message ?: "Network error occurred", e)
            }
        }
    }

    override suspend fun confirmPickup(
        applicationId: String,
        receivedQuantity: Int,
        conditionRating: Int,
        feedback: String?
    ): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val token = preferencesManager.getAuthToken()
                if (token == null) {
                    return@withContext Result.error<Boolean>("Not authenticated")
                }

                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                val currentDate = Date()

                val request = ConfirmPickupRequest(
                    pickupDate = dateFormat.format(currentDate),
                    pickupTime = timeFormat.format(currentDate),
                    receivedQuantity = receivedQuantity,
                    conditionRating = conditionRating,
                    feedback = feedback
                )

                val response = fertilizerApi.confirmPickup("Bearer $token", applicationId, request)

                if (response.isSuccessful && response.body()?.success == true) {
                    fertilizerDao.updateApplicationStatus(applicationId, "COMPLETED")
                    Result.success(true)
                } else {
                    Result.error<Boolean>("Failed to confirm pickup")
                }
            } catch (e: Exception) {
                Result.error<Boolean>(e.message ?: "Network error occurred", e)
            }
        }
    }

    override suspend fun reportApplicationIssue(
        applicationId: String,
        issueType: String,
        description: String,
        suggestedResolution: String?,
        attachments: List<String>?
    ): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val token = preferencesManager.getAuthToken()
                if (token == null) {
                    return@withContext Result.error<Boolean>("Not authenticated")
                }

                val request = ReportIssueRequest(
                    deliveryId = applicationId,
                    issueType = issueType,
                    description = description,
                    suggestedResolution = suggestedResolution,
                    attachments = attachments
                )
                val response = fertilizerApi.reportApplicationIssue("Bearer $token", applicationId, request)

                if (response.isSuccessful && response.body()?.success == true) {
                    Result.success(true)
                } else {
                    Result.error<Boolean>("Failed to report issue")
                }
            } catch (e: Exception) {
                Result.error<Boolean>(e.message ?: "Network error occurred", e)
            }
        }
    }

    override suspend fun getPendingApplicationCount(): Int {
        return withContext(Dispatchers.IO) {
            fertilizerDao.getPendingApplicationCount()
        }
    }

    override suspend fun getTotalApprovedQuantity(): Int {
        return withContext(Dispatchers.IO) {
            fertilizerDao.getTotalApprovedQuantity() ?: 0
        }
    }

    override fun checkApplicationEligibility(season: String): Flow<Result<CheckApplicationEligibilityUseCase.EligibilityResult>> {
        TODO("Not yet implemented")
    }

    private fun FertilizerEntity.toDomainModel(): FertilizerApplication {
        return FertilizerApplication(
            id = this.id,
            season = this.season,
            plotSize = this.plotSize,
            quantityRequested = this.quantityRequested,
            quantityApproved = this.quantityApproved,
            recommendedQuantity = this.recommendedQuantity,
            status = ApplicationStatus.valueOf(this.status),
            applicationDate = this.applicationDate,
            approvalDate = this.approvalDate,
            pickupDate = this.pickupDate,
            completionDate = this.completionDate,
            approvedBy = this.approvedBy,
            approverId = this.approverId,
            rejectionReason = this.rejectionReason,
            pickupLocation = this.pickupLocation,
            notes = this.notes
        )
    }

    private fun FertilizerApplicationDto.toEntity(): FertilizerEntity {
        val userId = preferencesManager.getUserId() ?: ""
        return FertilizerEntity(
            id = this.id,
            userId = userId,
            season = this.season,
            plotSize = this.plotSize,
            quantityRequested = this.quantityRequested,
            quantityApproved = this.quantityApproved ?: 0,
            recommendedQuantity = this.recommendedQuantity,
            status = this.status,
            applicationDate = parseDateToTimestamp(this.appliedDate),
            approvalDate = this.approvalDate?.let { parseDateToTimestamp(it) },
            pickupDate = this.pickupDate?.let { parseDateToTimestamp(it) },
            completionDate = null,
            approvedBy = null,
            approverId = null,
            rejectionReason = this.rejectionReason,
            pickupLocation = this.pickupLocation,
            notes = null,
            createdAt = parseDateToTimestamp(this.createdAt),
            updatedAt = this.updatedAt?.let { parseDateToTimestamp(it) } ?: System.currentTimeMillis(),
            syncedToServer = true,
            serverId = this.id
        )
    }

    private fun calculateRecommendedQuantity(plotSize: Double): Int {
        return (plotSize * 10).toInt()
    }

    private fun parseDateToTimestamp(dateString: String): Long {
        return try {
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            format.parse(dateString)?.time ?: System.currentTimeMillis()
        } catch (e: Exception) {
            System.currentTimeMillis()
        }
    }
}