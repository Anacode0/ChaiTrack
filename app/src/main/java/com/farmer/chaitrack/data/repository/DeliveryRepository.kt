package com.farmer.chaitrack.data.repository

import com.farmer.chaitrack.data.local.database.dao.DeliveryDao
import com.farmer.chaitrack.data.local.entity.DeliveryEntity
import com.farmer.chaitrack.data.local.preferences.PreferencesManager
import com.farmer.chaitrack.data.remote.api.DeliveryApi
import com.farmer.chaitrack.data.remote.dto.*
import com.farmer.chaitrack.domain.model.DeliveryItem
import com.farmer.chaitrack.util.Result
import com.farmer.chaitrack.util.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeliveryRepository @Inject constructor(
    private val deliveryApi: DeliveryApi,
    private val deliveryDao: DeliveryDao,
    private val preferencesManager: PreferencesManager,
    private val dispatcher: CoroutineDispatcher
) {

    fun getDeliveriesFlow(): Flow<List<DeliveryItem>> {
        return deliveryDao.getAllDeliveries().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    suspend fun getDeliveryById(deliveryId: String): DeliveryItem? {
        return withContext(dispatcher) {
            deliveryDao.getDeliveryById(deliveryId)?.toDomainModel()
        }
    }

    fun getDeliveriesByDateRange(startDate: Long, endDate: Long): Flow<List<DeliveryItem>> {
        return deliveryDao.getDeliveriesByDateRange(startDate, endDate).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    fun getDeliveriesByMonth(yearMonth: String): Flow<List<DeliveryItem>> {
        return deliveryDao.getDeliveriesByMonth(yearMonth).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    fun getDeliveriesByYear(year: String): Flow<List<DeliveryItem>> {
        return deliveryDao.getDeliveriesByYear(year).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    suspend fun fetchAndSyncDeliveries(): Result<List<DeliveryItem>> {
        return withContext(dispatcher) {
            try {
                val token = preferencesManager.getAuthToken()
                    ?: return@withContext Result.Error("Not authenticated")

                val response = deliveryApi.getDeliveries("Bearer $token")

                if (response.isSuccessful && response.body()?.success == true) {
                    val deliveries = response.body()?.data?.deliveries ?: emptyList()

                    val entities = deliveries.map { it.toEntity() }
                    deliveryDao.insertDeliveries(entities)

                    preferencesManager.updateLastSyncTime()

                    Result.Success(entities.map { it.toDomainModel() })
                } else {
                    Result.Error("Failed to fetch deliveries")
                }
            } catch (e: Exception) {
                Result.Error(e.message ?: "Network error occurred")
            }
        }
    }

    suspend fun getDeliveryStatistics(
        startDate: String? = null,
        endDate: String? = null
    ): Result<DeliveryStatisticsData> {
        return withContext(dispatcher) {
            try {
                val token = preferencesManager.getAuthToken()
                    ?: return@withContext Result.Error("Not authenticated")

                val response =
                    deliveryApi.getDeliveryStatistics("Bearer $token", startDate, endDate)

                if (response.isSuccessful && response.body()?.success == true) {
                    val stats = response.body()?.data
                    if (stats != null) {
                        Result.Success(stats)
                    } else {
                        Result.Error("Invalid statistics data")
                    }
                } else {
                    Result.Error("Failed to fetch statistics")
                }
            } catch (e: Exception) {
                Result.Error(e.message ?: "Network error occurred")
            }
        }
    }

    suspend fun getMonthlyDeliverySummary(
        year: Int,
        month: Int
    ): Result<MonthlyDeliverySummaryData> {
        return withContext(dispatcher) {
            try {
                val token = preferencesManager.getAuthToken()
                    ?: return@withContext Result.Error("Not authenticated")

                val response = deliveryApi.getMonthlyDeliverySummary("Bearer $token", year, month)

                if (response.isSuccessful && response.body()?.success == true) {
                    val summary = response.body()?.data
                    if (summary != null) {
                        Result.Success(summary)
                    } else {
                        Result.Error("Invalid summary data")
                    }
                } else {
                    Result.Error("Failed to fetch summary")
                }
            } catch (e: Exception) {
                Result.Error(e.message ?: "Network error occurred")
            }
        }
    }

    suspend fun getTotalWeightByDateRange(startDate: Long, endDate: Long): Double {
        return withContext(dispatcher) {
            deliveryDao.getTotalWeightByDateRange(startDate, endDate) ?: 0.0
        }
    }

    suspend fun getTotalWeightByMonth(yearMonth: String): Double {
        return withContext(dispatcher) {
            deliveryDao.getTotalWeightByMonth(yearMonth) ?: 0.0
        }
    }

    suspend fun reportIssue(deliveryId: String, issueType: String, description: String): Result<Boolean> {
        return withContext(dispatcher) {
            try {
                val token = preferencesManager.getAuthToken()
                    ?: return@withContext Result.Error("Not authenticated")

                val request = ReportIssueRequest(
                    deliveryId = deliveryId,
                    issueType = issueType,
                    description = description
                )

                val response = deliveryApi.reportDeliveryIssue("Bearer $token", request)

                if (response.isSuccessful && response.body()?.success == true) {
                    Result.Success(true)
                } else {
                    Result.Error("Failed to report issue")
                }
            } catch (e: Exception) {
                Result.Error(e.message ?: "Network error occurred")
            }
        }
    }

    suspend fun getCurrentPrice(): Result<Double> {
        return withContext(dispatcher) {
            try {
                val token = preferencesManager.getAuthToken()
                    ?: return@withContext Result.Error("Not authenticated")

                val response = deliveryApi.getCurrentPrice("Bearer $token")

                if (response.isSuccessful && response.body()?.success == true) {
                    val price = response.body()?.data?.pricePerKg
                    if (price != null) {
                        Result.Success(price)
                    } else {
                        Result.Error("Invalid price data")
                    }
                } else {
                    Result.Error("Failed to fetch price")
                }
            } catch (e: Exception) {
                Result.Error(e.message ?: "Network error occurred")
            }
        }
    }

    suspend fun exportToPdf(startDate: String? = null, endDate: String? = null): Result<ByteArray> {
        return withContext(dispatcher) {
            try {
                val token = preferencesManager.getAuthToken()
                    ?: return@withContext Result.Error("Not authenticated")

                val response =
                    deliveryApi.exportDeliveriesToPdf("Bearer $token", startDate, endDate)

                if (response.isSuccessful) {
                    val bytes = response.body()?.bytes()
                    if (bytes != null) {
                        Result.Success(bytes)
                    } else {
                        Result.Error("Failed to download PDF")
                    }
                } else {
                    Result.Error("Failed to export deliveries")
                }
            } catch (e: Exception) {
                Result.Error(e.message ?: "Network error occurred")
            }
        }
    }

    suspend fun insertDelivery(delivery: DeliveryEntity) {
        withContext(dispatcher) {
            deliveryDao.insertDelivery(delivery)
        }
    }

    suspend fun updateDelivery(delivery: DeliveryEntity) {
        withContext(dispatcher) {
            deliveryDao.updateDelivery(delivery)
        }
    }

    suspend fun deleteDelivery(delivery: DeliveryEntity) {
        withContext(dispatcher) {
            deliveryDao.deleteDelivery(delivery)
        }
    }

    suspend fun getDeliveryCount(): Int {
        return withContext(dispatcher) {
            deliveryDao.getDeliveryCount()
        }
    }

    suspend fun deleteAllDeliveries() {
        withContext(dispatcher) {
            deliveryDao.deleteAllDeliveries()
        }
    }
}