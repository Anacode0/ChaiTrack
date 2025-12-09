package com.farmer.chaitrack.data.local.database.dao

import androidx.room.*
import com.farmer.chaitrack.data.local.entity.FinanceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FinanceDao {

    @Query("SELECT * FROM finance_records ORDER BY year DESC, month DESC")
    fun getAllFinanceRecords(): Flow<List<FinanceEntity>>

    @Query("SELECT * FROM finance_records WHERE id = :recordId")
    suspend fun getFinanceRecordById(recordId: String): FinanceEntity?

    @Query("SELECT * FROM finance_records WHERE year = :year AND month = :month")
    suspend fun getFinanceRecordByYearMonth(year: Int, month: Int): FinanceEntity?

    @Query("""
        SELECT * FROM finance_records 
        WHERE year = :year 
        ORDER BY month DESC
    """)
    fun getFinanceRecordsByYear(year: Int): Flow<List<FinanceEntity>>

    @Query("""
        SELECT * FROM finance_records 
        WHERE year >= :startYear AND year <= :endYear
        ORDER BY year DESC, month DESC
    """)
    fun getFinanceRecordsByYearRange(startYear: Int, endYear: Int): Flow<List<FinanceEntity>>

    @Query("""
        SELECT SUM(totalEarnings) FROM finance_records 
        WHERE year = :year
    """)
    suspend fun getTotalEarningsByYear(year: Int): Double?

    @Query("""
        SELECT SUM(totalWeight) FROM finance_records 
        WHERE year = :year
    """)
    suspend fun getTotalWeightByYear(year: Int): Double?

    @Query("""
        SELECT AVG(pricePerKg) FROM finance_records 
        WHERE year = :year
    """)
    suspend fun getAveragePriceByYear(year: Int): Double?

    @Query("""
        SELECT * FROM finance_records 
        WHERE pdfGenerated = 1 
        ORDER BY year DESC, month DESC
    """)
    fun getRecordsWithPdf(): Flow<List<FinanceEntity>>

    @Query("""
        SELECT * FROM finance_records 
        WHERE year = :year AND month = :month AND pdfGenerated = 1
    """)
    suspend fun getPdfRecord(year: Int, month: Int): FinanceEntity?

    @Query("SELECT pricePerKg FROM finance_records WHERE year = :year AND month = :month")
    suspend fun getPricePerKgForMonth(year: Int, month: Int): Double?

    @Query("""
        SELECT pricePerKg FROM finance_records 
        ORDER BY year DESC, month DESC 
        LIMIT 1
    """)
    suspend fun getLatestPricePerKg(): Double?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFinanceRecord(record: FinanceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFinanceRecords(records: List<FinanceEntity>)

    @Update
    suspend fun updateFinanceRecord(record: FinanceEntity)

    @Query("""
        UPDATE finance_records 
        SET pdfGenerated = :pdfGenerated, 
            pdfFilePath = :pdfFilePath, 
            pdfGeneratedAt = :generatedAt 
        WHERE id = :recordId
    """)
    suspend fun updatePdfInfo(
        recordId: String,
        pdfGenerated: Boolean,
        pdfFilePath: String?,
        generatedAt: Long?
    )

    @Query("""
        UPDATE finance_records 
        SET totalWeight = :totalWeight, 
            totalEarnings = :totalEarnings, 
            updatedAt = :updatedAt 
        WHERE year = :year AND month = :month
    """)
    suspend fun updateMonthlyTotals(
        year: Int,
        month: Int,
        totalWeight: Double,
        totalEarnings: Double,
        updatedAt: Long
    )

    @Delete
    suspend fun deleteFinanceRecord(record: FinanceEntity)

    @Query("DELETE FROM finance_records WHERE id = :recordId")
    suspend fun deleteFinanceRecordById(recordId: String)

    @Query("DELETE FROM finance_records WHERE year = :year AND month = :month")
    suspend fun deleteFinanceRecordByYearMonth(year: Int, month: Int)

    @Query("DELETE FROM finance_records")
    suspend fun deleteAllFinanceRecords()

    @Query("SELECT COUNT(*) FROM finance_records")
    suspend fun getFinanceRecordCount(): Int

    @Query("SELECT COUNT(DISTINCT year) FROM finance_records")
    suspend fun getYearsWithRecordsCount(): Int
}