package com.farmer.chaitrack.domain.repository

import com.farmer.chaitrack.data.remote.dto.*
import com.farmer.chaitrack.domain.model.PaymentRecord
import com.farmer.chaitrack.domain.model.FinanceRecord
import com.farmer.chaitrack.domain.model.PriceHistory
import com.farmer.chaitrack.domain.model.MonthlyTranscript
import com.farmer.chaitrack.domain.model.PriceHistoryItem
import com.farmer.chaitrack.util.Result
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.time.LocalDate

interface FinanceRepository {

    fun getFinanceRecordsFlow(): Flow<List<FinanceRecord>>
    suspend fun getPriceHistory(startDate: LocalDate, endDate: LocalDate): Flow<Result<List<PriceHistory>>>
    fun getAllPriceHistory(): Flow<Result<List<PriceHistory>>>
    fun getRecentPriceHistory(): Flow<Result<List<PriceHistory>>>
    suspend fun getTranscriptByPeriod(startDate: LocalDate, endDate: LocalDate): Flow<Result<MonthlyTranscript>>
    suspend fun getTranscriptByMonthYear(month: Int, year: Int): Flow<Result<MonthlyTranscript>>
    fun getAvailableYears(): Flow<Result<List<Int>>>
    suspend fun getAvailableYearsOnce(): Result<List<Int>>
    fun getPaymentHistory(): Flow<Result<List<PaymentRecord>>>
    fun getPaymentHistoryByPeriod(startDate: Long, endDate: Long): Flow<Result<List<PaymentRecord>>>
    suspend fun getPaymentRecordById(paymentId: String): Flow<Result<PaymentRecord>>
    fun getRecentPayments(limit: Int = 10): Flow<Result<List<PaymentRecord>>>
    fun getPricePerKg(): Flow<Result<Double>>
    suspend fun getMonthlyTranscript(month: Int, year: Int): Flow<Result<MonthlyTranscript>>
    suspend fun getMonthlyTranscriptById(id: String): Flow<Result<MonthlyTranscript>>
    fun getFinanceRecordsByType(type: String): Flow<Result<List<FinanceRecord>>>
    fun getFinanceRecords(): Flow<Result<List<FinanceRecord>>>
    fun getFinanceRecordsByPeriod(startDate: Long, endDate: Long): Flow<Result<List<FinanceRecord>>>
    fun getCurrentMonthProjection(): Flow<Result<Double>>
    suspend fun getPricePerKgOnce(): Result<Double>
    fun getEstimatedEarningsForPeriod(startDate: LocalDate, endDate: LocalDate): Flow<Result<Double>>
    fun getEstimatedEarnings(): Flow<Result<Double>>
    fun getPriceHistory(startDate: Long, endDate: Long): Flow<Result<List<PriceHistoryItem>>>
    fun getFinanceRecordsByYear(year: Int): Flow<List<FinanceRecord>>
    fun getAvailableMonthsFlow(): Flow<Result<List<MonthDto>>>

    suspend fun getAvailableMonths(): Result<List<MonthDto>>
    suspend fun getAvailableMonths(year: Int): Result<List<MonthDto>>
    suspend fun getFinanceRecordByMonth(year: Int, month: Int): FinanceRecord?
    suspend fun getLatestPrice(): Double
    suspend fun getTotalEarningsByYear(year: Int): Double
    suspend fun fetchAndSyncFinanceRecords(): Result<List<FinanceRecord>>
    suspend fun getFinancialSummary(startYear: Int?, endYear: Int?): Result<FinancialSummaryData>
    suspend fun getMonthlyEarnings(year: Int, month: Int): Result<MonthlyEarningsData>
    suspend fun getPriceForPeriod(year: Int, month: Int): Result<Double>
    suspend fun generateTranscript(year: Int, month: Int): Result<GenerateTranscriptResponse>
    suspend fun downloadTranscript(year: Int, month: Int, destinationFile: File): Result<File>
    suspend fun shareTranscript(transcriptId: String, email: String): Result<Boolean>
    suspend fun updateBankAccount(accountNumber: String, bankName: String): Result<Boolean>
    suspend fun reportFinancialIssue(description: String): Result<Boolean>
}