package com.farmer.chaitrack.domain.usecase.delivery

import com.farmer.chaitrack.data.repository.DeliveryRepository
import com.farmer.chaitrack.domain.model.DeliveryItem
import com.farmer.chaitrack.ui.delivery.DeliveryFilter
import com.farmer.chaitrack.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Calendar
import javax.inject.Inject

class GetDeliveriesUseCase @Inject constructor(
    private val deliveryRepository: DeliveryRepository
) {
    operator fun invoke(
        filter: DeliveryFilter = DeliveryFilter.THIS_MONTH,
        forceRefresh: Boolean = false
    ): Flow<Result<List<DeliveryItem>>> = flow {
        emit(Result.Loading)

        try {
            val dateRange: Pair<Long, Long> = filter.getDateRange()
            val startDate = dateRange.first
            val endDate = dateRange.second

            val deliveriesFlow = if (forceRefresh) {
                deliveryRepository.fetchAndSyncDeliveries()
                deliveryRepository.getDeliveriesByDateRange(startDate, endDate)
            } else {
                deliveryRepository.getDeliveriesByDateRange(startDate, endDate)
            }

            deliveriesFlow.collect { deliveries ->
                emit(Result.Success(deliveries))
            }
        } catch (e: Exception) {
            emit(Result.Error(message = e.message ?: "Failed to load deliveries. Please try again."))
        }
    }

    fun getDeliveriesByYear(year: Int): Flow<Result<List<DeliveryItem>>> = flow {
        emit(Result.Loading)

        try {
            val calendar = Calendar.getInstance()

            calendar.set(year, Calendar.JANUARY, 1, 0, 0, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val startDate = calendar.timeInMillis

            calendar.set(year, Calendar.DECEMBER, 31, 23, 59, 59)
            calendar.set(Calendar.MILLISECOND, 999)
            val endDate = calendar.timeInMillis

            val deliveriesFlow = deliveryRepository.getDeliveriesByDateRange(startDate, endDate)
            deliveriesFlow.collect { deliveries ->
                emit(Result.Success(deliveries))
            }
        } catch (e: Exception) {
            emit(Result.Error(message = e.message ?: "Failed to load deliveries for $year"))
        }
    }

    fun getTodayDeliveries(): Flow<Result<List<DeliveryItem>>> = flow {
        emit(Result.Loading)

        try {
            val calendar = Calendar.getInstance()

            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val startDate = calendar.timeInMillis
            calendar.set(Calendar.HOUR_OF_DAY, 23)
            calendar.set(Calendar.MINUTE, 59)
            calendar.set(Calendar.SECOND, 59)
            calendar.set(Calendar.MILLISECOND, 999)
            val endDate = calendar.timeInMillis

            deliveryRepository.fetchAndSyncDeliveries()
            val deliveriesFlow = deliveryRepository.getDeliveriesByDateRange(startDate, endDate)
            deliveriesFlow.collect { deliveries ->
                emit(Result.Success(deliveries))
            }
        } catch (e: Exception) {
            emit(Result.Error(message = e.message ?: "Failed to load today's deliveries"))
        }
    }

    fun getDeliveriesByMonth(yearMonth: String): Flow<Result<List<DeliveryItem>>> = flow {
        emit(Result.Loading)

        try {
            val deliveriesFlow = deliveryRepository.getDeliveriesByMonth(yearMonth)
            deliveriesFlow.collect { deliveries ->
                emit(Result.Success(deliveries))
            }
        } catch (e: Exception) {
            emit(Result.Error(message = e.message ?: "Failed to load deliveries for $yearMonth"))
        }
    }

    fun calculateTotalWeight(deliveries: List<DeliveryItem>): Double {
        return deliveries.sumOf { it.weight }
    }

    fun calculateTotalEarnings(deliveries: List<DeliveryItem>, pricePerKg: Double): Double {
        return calculateTotalWeight(deliveries) * pricePerKg
    }

    fun getMostRecentDelivery(deliveries: List<DeliveryItem>): DeliveryItem? {
        return deliveries.maxByOrNull { it.date }
    }

    fun filterByClerk(deliveries: List<DeliveryItem>, clerkName: String): List<DeliveryItem> {
        return deliveries.filter {
            it.clerkName.contains(clerkName, ignoreCase = true)
        }
    }

    fun getAvailableYears(firstLoginYear: Int): List<Int> {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        return (firstLoginYear..currentYear).toList().reversed()
    }

    suspend fun getTotalWeightByDateRange(startDate: Long, endDate: Long): Double {
        return deliveryRepository.getTotalWeightByDateRange(startDate, endDate)
    }

    suspend fun getTotalWeightByMonth(yearMonth: String): Double {
        return deliveryRepository.getTotalWeightByMonth(yearMonth)
    }
}