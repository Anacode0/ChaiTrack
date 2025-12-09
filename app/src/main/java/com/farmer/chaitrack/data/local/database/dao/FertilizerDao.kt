package com.farmer.chaitrack.data.local.database.dao

import androidx.room.*
import com.farmer.chaitrack.data.local.entity.FertilizerEntity
import com.farmer.chaitrack.domain.model.FertilizerApplication
import kotlinx.coroutines.flow.Flow

@Dao
interface FertilizerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(application: FertilizerEntity): Long

    @Query("SELECT * FROM fertilizer_applications ORDER BY createdAt DESC")
    fun getAllApplications(): Flow<List<FertilizerEntity>>

    @Query("SELECT * FROM fertilizer_applications WHERE id = :applicationId")
    suspend fun getApplicationById(applicationId: String): FertilizerEntity?

    @Query("""
        SELECT * FROM fertilizer_applications 
        WHERE status = :status 
        ORDER BY createdAt DESC
    """)
    fun getApplicationsByStatus(status: String): Flow<List<FertilizerEntity>>

    @Query("""
        SELECT * FROM fertilizer_applications 
        WHERE season = :season 
        ORDER BY createdAt DESC
    """)
    fun getApplicationsBySeason(season: String): Flow<List<FertilizerEntity>>

    @Query("""
        SELECT * FROM fertilizer_applications 
        WHERE strftime('%Y', datetime(createdAt/1000, 'unixepoch')) = :year
        ORDER BY createdAt DESC
    """)
    fun getApplicationsByYear(year: String): Flow<List<FertilizerEntity>>

    @Query("""
        SELECT * FROM fertilizer_applications 
        WHERE status IN ('PENDING_REVIEW','UNDER_REVIEW', 'APPROVED', 'READY_FOR_PICKUP', 'PICKED_UP', 'REJECTED')
        ORDER BY createdAt DESC 
        LIMIT 1
    """)
    suspend fun getCurrentActiveApplication(): FertilizerEntity?

    @Query("""
        SELECT * FROM fertilizer_applications 
        WHERE status = 'COMPLETED' 
        ORDER BY createdAt DESC
    """)
    fun getApplicationHistory(): Flow<List<FertilizerEntity>>

    @Query("SELECT COUNT(*) FROM fertilizer_applications WHERE status = 'PENDING_REVIEW'")
    suspend fun getPendingApplicationCount(): Int

    @Query("SELECT SUM(quantityRequested) FROM fertilizer_applications WHERE status = 'APPROVED'")
    suspend fun getTotalApprovedQuantity(): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApplication(application: FertilizerEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApplications(applications: List<FertilizerEntity>)

    @Update
    suspend fun updateApplication(application: FertilizerEntity)

    @Query("UPDATE fertilizer_applications SET status = :status WHERE id = :applicationId")
    suspend fun updateApplicationStatus(applicationId: String, status: String)

    @Query("""
        UPDATE fertilizer_applications 
        SET status = :status, quantityApproved = :quantityApproved, updatedAt = :updatedAt
        WHERE id = :applicationId
    """)
    suspend fun approveApplication(
        applicationId: String,
        status: String,
        quantityApproved: Int,
        updatedAt: Long
    )

    @Delete
    suspend fun deleteApplication(application: FertilizerEntity)

    @Query("DELETE FROM fertilizer_applications WHERE id = :applicationId")
    suspend fun deleteApplicationById(applicationId: String)

    @Query("DELETE FROM fertilizer_applications")
    suspend fun deleteAllApplications()

    @Query("SELECT COUNT(*) FROM fertilizer_applications")
    suspend fun getApplicationCount(): Int
}