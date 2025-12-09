package com.farmer.chaitrack.data.local.database.dao

import androidx.room.*
import com.farmer.chaitrack.data.local.entity.DeliveryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DeliveryDao {

    @Query("SELECT * FROM deliveries ORDER BY date DESC, time DESC")
    fun getAllDeliveries(): Flow<List<DeliveryEntity>>

    @Query("SELECT * FROM deliveries WHERE id = :deliveryId")
    suspend fun getDeliveryById(deliveryId: String): DeliveryEntity?

    @Query("""
        SELECT * FROM deliveries 
        WHERE date >= :startDate AND date <= :endDate 
        ORDER BY date DESC, time DESC
    """)
    fun getDeliveriesByDateRange(startDate: Long, endDate: Long): Flow<List<DeliveryEntity>>

    @Query("""
        SELECT * FROM deliveries 
        WHERE strftime('%Y', datetime(date/1000, 'unixepoch')) = :year
        ORDER BY date DESC, time DESC
    """)
    fun getDeliveriesByYear(year: String): Flow<List<DeliveryEntity>>

    @Query("""
        SELECT * FROM deliveries 
        WHERE strftime('%Y-%m', datetime(date/1000, 'unixepoch')) = :yearMonth
        ORDER BY date DESC, time DESC
    """)
    fun getDeliveriesByMonth(yearMonth: String): Flow<List<DeliveryEntity>>

    @Query("SELECT SUM(weight) FROM deliveries WHERE date >= :startDate AND date <= :endDate")
    suspend fun getTotalWeightByDateRange(startDate: Long, endDate: Long): Double?

    @Query("""
        SELECT SUM(weight) FROM deliveries 
        WHERE strftime('%Y-%m', datetime(date/1000, 'unixepoch')) = :yearMonth
    """)
    suspend fun getTotalWeightByMonth(yearMonth: String): Double?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDelivery(delivery: DeliveryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeliveries(deliveries: List<DeliveryEntity>)

    @Update
    suspend fun updateDelivery(delivery: DeliveryEntity)

    @Delete
    suspend fun deleteDelivery(delivery: DeliveryEntity)

    @Query("DELETE FROM deliveries")
    suspend fun deleteAllDeliveries()

    @Query("SELECT COUNT(*) FROM deliveries")
    suspend fun getDeliveryCount(): Int
}