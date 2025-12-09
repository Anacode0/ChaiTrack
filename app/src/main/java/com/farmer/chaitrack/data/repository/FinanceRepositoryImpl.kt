package com.farmer.chaitrack.data.repository

import com.farmer.chaitrack.data.local.database.dao.FinanceDao
import com.farmer.chaitrack.data.local.entity.FinanceEntity
import com.farmer.chaitrack.data.local.preferences.PreferencesManager
import com.farmer.chaitrack.data.remote.api.FinanceApi
import com.farmer.chaitrack.data.remote.dto.*
import com.farmer.chaitrack.domain.model.*
import com.farmer.chaitrack.domain.repository.FinanceRepository
import com.farmer.chaitrack.util.Result
import dagger.Binds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FinanceRepositoryImpl @Inject constructor(
    private val financeApi: FinanceApi,
    private val financeDao: FinanceDao,
    private val preferencesManager: PreferencesManager
) : FinanceRepository {

    private val defaultDispatcher = Dispatchers.IO


    override fun getFinanceRecordsFlow(): Flow<List<FinanceRecord>> {
        return financeDao.getAllFinanceRecords().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun getPriceHistory(
        startDate: java.time.LocalDate,
        endDate: java.time.LocalDate
    ): Flow<Result<List<PriceHistory>>> {
        TODO("Not yet implemented")
    }

    override fun getFinanceRecordsByYear(year: Int): Flow<List<FinanceRecord>> {
        return financeDao.getFinanceRecordsByYear(year).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getAvailableMonthsFlow(): Flow<Result<List<MonthDto>>> = flow {
        try {
            emit(Result.Loading)
            val months = getAvailableMonths()
            when (months) {
                is Result.Success -> emit(Result.Success(months.data))
                is Result.Error -> emit(Result.Error(months.message))
                is Result.Loading -> emit(Result.Loading)
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Failed to get available months"))
        }
    }

    suspend fun getPriceHistory(startDate: LocalDate, endDate: LocalDate): Flow<Result<List<PriceHistory>>> = flow {
        emit(Result.Loading)
        try {
            val priceHistory = emptyList<PriceHistory>()
            emit(Result.Success(priceHistory))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Failed to get price history"))
        }
    }

    override fun getPriceHistory(startDate: Long, endDate: Long): Flow<Result<List<PriceHistoryItem>>> = flow {
        emit(Result.Loading)
        try {
            val priceHistoryItems = emptyList<PriceHistoryItem>()
            emit(Result.Success(priceHistoryItems))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Failed to get price history"))
        }
    }

    override fun getAllPriceHistory(): Flow<Result<List<PriceHistory>>> = flow {
        emit(Result.Loading)
        try {
            val priceHistory = emptyList<PriceHistory>()
            emit(Result.Success(priceHistory))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Failed to get price history"))
        }
    }

    override fun getRecentPriceHistory(): Flow<Result<List<PriceHistory>>> = flow {
        emit(Result.Loading)
        try {
            val priceHistory = emptyList<PriceHistory>()
            emit(Result.Success(priceHistory))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Failed to get recent price history"))
        }
    }

    override suspend fun getTranscriptByPeriod(
        startDate: java.time.LocalDate,
        endDate: java.time.LocalDate
    ): Flow<Result<MonthlyTranscript>> {
        TODO("Not yet implemented")
    }

    suspend fun getTranscriptByPeriod(startDate: LocalDate, endDate: LocalDate): Flow<Result<MonthlyTranscript>> = flow {
        emit(Result.Loading)
        try {
            val allRecords = mutableListOf<FinanceRecord>()
            financeDao.getAllFinanceRecords().collect { entities ->
                allRecords.addAll(entities.map { it.toDomainModel() })
            }

            val recordsInPeriod = allRecords.filter { record ->
                val recordDate = LocalDate.of(record.year, record.month, 1)
                !recordDate.isBefore(startDate) && !recordDate.isAfter(endDate)
            }

            val transcript = MonthlyTranscript(
                id = "${startDate.monthValue}-${startDate.year}-${endDate.monthValue}-${endDate.year}",
                month = startDate.monthValue,
                year = startDate.year,
                totalWeight = recordsInPeriod.sumOf { it.totalWeight },
                pricePerKg = recordsInPeriod.firstOrNull()?.pricePerKg ?: 0.0,
                totalEarnings = recordsInPeriod.sumOf { it.totalEarnings },
                farmerId = preferencesManager.getUserId(),
                collectionCenter = null,
                records = recordsInPeriod,
                timestamp = System.currentTimeMillis(),
                isSynced = false
            )
            emit(Result.Success(transcript))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Failed to get transcript for period"))
        }
    }

    override suspend fun getTranscriptByMonthYear(month: Int, year: Int): Flow<Result<MonthlyTranscript>> = flow {
        emit(Result.Loading)
        try {
            val record = financeDao.getFinanceRecordByYearMonth(year, month)
            val records = if (record != null) listOf(record.toDomainModel()) else emptyList()

            val transcript = MonthlyTranscript(
                id = record?.id ?: "",
                month = month,
                year = year,
                totalWeight = record?.totalWeight ?: 0.0,
                pricePerKg = record?.pricePerKg ?: 0.0,
                totalEarnings = record?.totalEarnings ?: 0.0,
                farmerId = preferencesManager.getUserId(),
                collectionCenter = null,
                records = records,
                timestamp = System.currentTimeMillis(),
                isSynced = record != null
            )
            emit(Result.Success(transcript))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Failed to get transcript"))
        }
    }

    override fun getAvailableYears(): Flow<Result<List<Int>>> = flow {
        emit(Result.Loading)
        try {
            val calendar = Calendar.getInstance()
            val currentYear = calendar.get(Calendar.YEAR)
            val years = (currentYear downTo currentYear - 5).toList()
            emit(Result.Success(years))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Failed to get available years"))
        }
    }

    override suspend fun getAvailableYearsOnce(): Result<List<Int>> {
        return withContext(defaultDispatcher) {
            try {
                val calendar = Calendar.getInstance()
                val currentYear = calendar.get(Calendar.YEAR)
                val years = (currentYear downTo currentYear - 5).toList()
                Result.Success(years)
            } catch (e: Exception) {
                Result.Error(e.message ?: "Failed to get available years")
            }
        }
    }

    override fun getPaymentHistory(): Flow<Result<List<PaymentRecord>>> = flow {
        emit(Result.Loading)
        try {
            val payments = emptyList<PaymentRecord>()
            emit(Result.Success(payments))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Failed to get payment history"))
        }
    }

    override fun getPaymentHistoryByPeriod(startDate: Long, endDate: Long): Flow<Result<List<PaymentRecord>>> = flow {
        emit(Result.Loading)
        try {
            val payments = emptyList<PaymentRecord>()
            emit(Result.Success(payments))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Failed to get payment history"))
        }
    }

    override suspend fun getPaymentRecordById(paymentId: String): Flow<Result<PaymentRecord>> = flow {
        emit(Result.Loading)
        try {
            val payment = PaymentRecord(
                id = paymentId,
                transactionId = paymentId,
                amount = 0.0,
                paymentDate = java.time.LocalDate.now(),
                paymentMethod = PaymentMethod.BANK_TRANSFER,
                status = PaymentStatus.PENDING,
                description = "",
                farmerId = preferencesManager.getUserId() ?: ""
            )
            emit(Result.Success(payment))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Failed to get payment record"))
        }
    }

    override fun getRecentPayments(limit: Int): Flow<Result<List<PaymentRecord>>> = flow {
        emit(Result.Loading)
        try {
            val payments = emptyList<PaymentRecord>() // TODO: Implement recent payments
            emit(Result.Success(payments))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Failed to get recent payments"))
        }
    }

    override fun getPricePerKg(): Flow<Result<Double>> = flow {
        emit(Result.Loading)
        try {
            val price = financeDao.getLatestPricePerKg() ?: 0.0
            emit(Result.Success(price))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Failed to get price per kg"))
        }
    }

    override suspend fun getMonthlyTranscript(month: Int, year: Int): Flow<Result<MonthlyTranscript>> = flow {
        emit(Result.Loading)
        try {
            val record = financeDao.getFinanceRecordByYearMonth(year, month)
            val records = if (record != null) listOf(record.toDomainModel()) else emptyList()

            val transcript = MonthlyTranscript(
                id = record?.id ?: "",
                month = month,
                year = year,
                totalWeight = record?.totalWeight ?: 0.0,
                pricePerKg = record?.pricePerKg ?: 0.0,
                totalEarnings = record?.totalEarnings ?: 0.0,
                farmerId = preferencesManager.getUserId(),
                collectionCenter = null,
                records = records,
                timestamp = System.currentTimeMillis(),
                isSynced = record != null
            )
            emit(Result.Success(transcript))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Failed to get monthly transcript"))
        }
    }

    override suspend fun getMonthlyTranscriptById(id: String): Flow<Result<MonthlyTranscript>> = flow {
        emit(Result.Loading)
        try {
            val record = financeDao.getFinanceRecordById(id)
            val records = if (record != null) listOf(record.toDomainModel()) else emptyList()

            val transcript = MonthlyTranscript(
                id = record?.id ?: id,
                month = record?.month ?: 1,
                year = record?.year ?: 2024,
                totalWeight = record?.totalWeight ?: 0.0,
                pricePerKg = record?.pricePerKg ?: 0.0,
                totalEarnings = record?.totalEarnings ?: 0.0,
                farmerId = preferencesManager.getUserId(),
                collectionCenter = null,
                records = records,
                timestamp = System.currentTimeMillis(),
                isSynced = record != null
            )
            emit(Result.Success(transcript))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Failed to get monthly transcript"))
        }
    }

    override fun getFinanceRecordsByType(type: String): Flow<Result<List<FinanceRecord>>> = flow {
        emit(Result.Loading)
        try {
            financeDao.getAllFinanceRecords().collect { entities ->
                val filtered = entities.map { it.toDomainModel() }.filter { it.type == type }
                emit(Result.Success(filtered))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Failed to get finance records by type"))
        }
    }

    override fun getFinanceRecords(): Flow<Result<List<FinanceRecord>>> = flow {
        emit(Result.Loading)
        try {
            financeDao.getAllFinanceRecords().collect { entities ->
                emit(Result.Success(entities.map { it.toDomainModel() }))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Failed to get finance records"))
        }
    }

    override fun getFinanceRecordsByPeriod(startDate: Long, endDate: Long): Flow<Result<List<FinanceRecord>>> = flow {
        emit(Result.Loading)
        try {
            financeDao.getAllFinanceRecords().collect { entities ->
                val filtered = entities.filter { entity ->
                    val recordDate = entity.createdAt ?: 0L
                    recordDate in startDate..endDate
                }.map { it.toDomainModel() }
                emit(Result.Success(filtered))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Failed to get finance records by period"))
        }
    }

    override fun getCurrentMonthProjection(): Flow<Result<Double>> = flow {
        emit(Result.Loading)
        try {
            val calendar = Calendar.getInstance()
            val currentYear = calendar.get(Calendar.YEAR)
            val currentMonth = calendar.get(Calendar.MONTH) + 1

            val record = financeDao.getFinanceRecordByYearMonth(currentYear, currentMonth)
            val projection = record?.totalEarnings ?: 0.0
            emit(Result.Success(projection))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Failed to get current month projection"))
        }
    }

    override suspend fun getPricePerKgOnce(): Result<Double> {
        return withContext(defaultDispatcher) {
            try {
                val price = financeDao.getLatestPricePerKg() ?: 0.0
                Result.Success(price)
            } catch (e: Exception) {
                Result.Error(e.message ?: "Failed to get price per kg")
            }
        }
    }

    override fun getEstimatedEarningsForPeriod(
        startDate: java.time.LocalDate,
        endDate: java.time.LocalDate
    ): Flow<Result<Double>> {
        TODO("Not yet implemented")
    }

    fun getEstimatedEarningsForPeriod(startDate: LocalDate, endDate: LocalDate): Flow<Result<Double>> = flow {
        emit(Result.Loading)
        try {
            val earnings = 0.0 // TODO: Implement estimated earnings calculation
            emit(Result.Success(earnings))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Failed to get estimated earnings"))
        }
    }

    override fun getEstimatedEarnings(): Flow<Result<Double>> = flow {
        emit(Result.Loading)
        try {
            val calendar = Calendar.getInstance()
            val currentYear = calendar.get(Calendar.YEAR)
            val currentMonth = calendar.get(Calendar.MONTH) + 1

            val record = financeDao.getFinanceRecordByYearMonth(currentYear, currentMonth)
            val earnings = record?.totalEarnings ?: 0.0
            emit(Result.Success(earnings))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Failed to get estimated earnings"))
        }
    }

    override suspend fun getAvailableMonths(): Result<List<MonthDto>> {
        return withContext(defaultDispatcher) {
            try {
                val calendar = Calendar.getInstance()
                val months = (0..11).map { i ->
                    calendar.time = Date()
                    calendar.add(Calendar.MONTH, -i)
                    val year = calendar.get(Calendar.YEAR)
                    val monthNum = calendar.get(Calendar.MONTH) + 1
                    MonthDto.from(year, monthNum).copy(hasData = true)
                }.sortedByDescending { it.year * 100 + it.monthNumber }

                Result.Success(months)
            } catch (e: Exception) {
                Result.Error(e.message ?: "Failed to get available months")
            }
        }
    }

    override suspend fun getAvailableMonths(year: Int): Result<List<MonthDto>> {
        return withContext(defaultDispatcher) {
            try {
                val months = (1..12).map { monthNum ->
                    MonthDto.from(year, monthNum).copy(hasData = true)
                }
                Result.Success(months)
            } catch (e: Exception) {
                Result.Error(e.message ?: "Failed to get available months for year $year")
            }
        }
    }


    override suspend fun getFinanceRecordByMonth(year: Int, month: Int): FinanceRecord? {
        return withContext(defaultDispatcher) {
            financeDao.getFinanceRecordByYearMonth(year, month)?.toDomainModel()
        }
    }

    override suspend fun getLatestPrice(): Double {
        return withContext(defaultDispatcher) {
            financeDao.getLatestPricePerKg() ?: 0.0
        }
    }

    override suspend fun getTotalEarningsByYear(year: Int): Double {
        return withContext(defaultDispatcher) {
            financeDao.getTotalEarningsByYear(year) ?: 0.0
        }
    }


    override suspend fun fetchAndSyncFinanceRecords(): Result<List<FinanceRecord>> {
        return withContext(defaultDispatcher) {
            try {
                val token = preferencesManager.getAuthToken()
                    ?: return@withContext Result.Error("Not authenticated")

                val response = financeApi.getFinanceRecords("Bearer $token")

                if (response.isSuccessful && response.body()?.success == true) {
                    val records = response.body()?.data?.records ?: emptyList()
                    val entities = records.map { it.toEntity() }
                    financeDao.insertFinanceRecords(entities)
                    preferencesManager.updateLastSyncTime()

                    Result.Success(entities.map { it.toDomainModel() })
                } else {
                    Result.Error("Failed to fetch finance records")
                }
            } catch (e: Exception) {
                Result.Error(e.message ?: "Network error occurred")
            }
        }
    }

    override suspend fun getFinancialSummary(startYear: Int?, endYear: Int?): Result<FinancialSummaryData> {
        return withContext(defaultDispatcher) {
            try {
                val token = preferencesManager.getAuthToken()
                    ?: return@withContext Result.Error("Not authenticated")

                val response = financeApi.getFinancialSummary("Bearer $token", startYear, endYear)

                if (response.isSuccessful && response.body()?.success == true) {
                    val summary = response.body()?.data
                    if (summary != null) {
                        Result.Success(summary)
                    } else {
                        Result.Error("Invalid summary data")
                    }
                } else {
                    Result.Error("Failed to fetch financial summary")
                }
            } catch (e: Exception) {
                Result.Error(e.message ?: "Network error occurred")
            }
        }
    }

    override suspend fun getMonthlyEarnings(year: Int, month: Int): Result<MonthlyEarningsData> {
        return withContext(defaultDispatcher) {
            try {
                val token = preferencesManager.getAuthToken()
                    ?: return@withContext Result.Error("Not authenticated")

                val response = financeApi.getMonthlyEarnings("Bearer $token", year, month)

                if (response.isSuccessful && response.body()?.success == true) {
                    val earningsDto = response.body()?.data
                    if (earningsDto != null) {
                        val monthlyEarningsData = MonthlyEarningsData(
                            month = earningsDto.month ?: month,
                            year = earningsDto.year ?: year,
                            totalEarnings = earningsDto.totalEarnings ?: 0.0,
                            totalWeight = earningsDto.totalWeight ?: 0.0,
                            pricePerKg = earningsDto.pricePerKg ?: 0.0,
                            deliveryCount = earningsDto.deliveryCount ?: 0,
                            deductions = earningsDto.deductions ?: 0.0,
                            netEarnings = earningsDto.netEarnings ?: (earningsDto.totalEarnings ?: 0.0),
                            paymentStatus = earningsDto.paymentStatus ?: "PENDING",
                            pdfGenerated = earningsDto.pdfGenerated ?: false,
                            pdfUrl = earningsDto.pdfUrl ?: ""
                        )
                        Result.Success(monthlyEarningsData)
                    } else {
                        Result.Error("Invalid earnings data")
                    }
                } else {
                    Result.Error("Failed to fetch monthly earnings")
                }
            } catch (e: Exception) {
                Result.Error(e.message ?: "Network error occurred")
            }
        }
    }

    override suspend fun getPriceForPeriod(year: Int, month: Int): Result<Double> {
        return withContext(defaultDispatcher) {
            try {
                val token = preferencesManager.getAuthToken()
                    ?: return@withContext Result.Error("Not authenticated")

                val response = financeApi.getPriceForPeriod("Bearer $token", year, month)

                if (response.isSuccessful && response.body()?.success == true) {
                    val priceData = response.body()?.data
                    if (priceData != null) {
                        val price = 0.0
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


    override suspend fun generateTranscript(year: Int, month: Int): Result<GenerateTranscriptResponse> {
        return withContext(defaultDispatcher) {
            try {
                val token = preferencesManager.getAuthToken()
                    ?: return@withContext Result.Error("Not authenticated")

                val request = GenerateTranscriptRequest(year = year, month = month)
                val response = financeApi.generateTranscript("Bearer $token", request)

                if (response.isSuccessful && response.body()?.success == true) {
                    val result = response.body()
                    if (result != null) {
                        updateLocalPdfInfo(year, month, result.data?.transcriptId, result.data?.pdfUrl)
                        Result.Success(result)
                    } else {
                        Result.Error("Invalid transcript response")
                    }
                } else {
                    Result.Error("Failed to generate transcript")
                }
            } catch (e: Exception) {
                Result.Error(e.message ?: "Network error occurred")
            }
        }
    }

    override suspend fun downloadTranscript(year: Int, month: Int, destinationFile: File): Result<File> {
        return withContext(defaultDispatcher) {
            try {
                val token = preferencesManager.getAuthToken()
                    ?: return@withContext Result.Error("Not authenticated")

                val response = financeApi.downloadTranscriptByMonth("Bearer $token", year, month)

                if (response.isSuccessful) {
                    val bytes = response.body()?.bytes()
                    if (bytes != null) {
                        destinationFile.writeBytes(bytes)
                        updateLocalPdfFilePath(year, month, destinationFile.absolutePath)
                        Result.Success(destinationFile)
                    } else {
                        Result.Error("Failed to download PDF")
                    }
                } else {
                    Result.Error("Failed to download transcript")
                }
            } catch (e: Exception) {
                Result.Error(e.message ?: "Network error occurred")
            }
        }
    }

    override suspend fun shareTranscript(transcriptId: String, email: String): Result<Boolean> {
        return withContext(defaultDispatcher) {
            try {
                val token = preferencesManager.getAuthToken()
                    ?: return@withContext Result.Error("Not authenticated")

                val deliveryId = getDeliveryIdForTranscript(transcriptId)

                val request = ShareTranscriptRequest(
                    email = email,
                    deliveryId = deliveryId
                )
                val response = financeApi.shareTranscript("Bearer $token", transcriptId, request)

                if (response.isSuccessful && response.body()?.success == true) {
                    Result.Success(true)
                } else {
                    Result.Error("Failed to share transcript")
                }
            } catch (e: Exception) {
                Result.Error(e.message ?: "Network error occurred")
            }
        }
    }

    private suspend fun getDeliveryIdForTranscript(transcriptId: String): String {
        // If transcriptId contains deliveryId info
        // return transcriptId.split("_").last()

        val financeRecord = financeDao.getFinanceRecordById(transcriptId)
        return financeRecord?.serverId ?: "default_delivery_id"

        // no deliveryId,modify API
        // return transcriptId
    }


    override suspend fun updateBankAccount(accountNumber: String, bankName: String): Result<Boolean> {
        return withContext(defaultDispatcher) {
            try {
                val token = preferencesManager.getAuthToken()
                    ?: return@withContext Result.Error("Not authenticated")

                val request = UpdateBankAccountRequest(
                    accountNumber = accountNumber,
                    bankName = bankName
                )
                val response = financeApi.updateBankAccount("Bearer $token", request)

                if (response.isSuccessful && response.body()?.success == true) {
                    Result.Success(true)
                } else {
                    Result.Error("Failed to update bank account")
                }
            } catch (e: Exception) {
                Result.Error(e.message ?: "Network error occurred")
            }
        }
    }

    override suspend fun reportFinancialIssue(description: String): Result<Boolean> {
        return withContext(defaultDispatcher) {
            try {
                val token = preferencesManager.getAuthToken()
                    ?: return@withContext Result.Error("Not authenticated")

                val deliveryId = "default_delivery_id"
                val issueType = "FINANCIAL"

                val request = ReportIssueRequest(
                    description = description,
                    deliveryId = deliveryId,
                    issueType = issueType
                )
                val response = financeApi.reportFinancialIssue("Bearer $token", request)

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


    private fun FinanceEntity.toDomainModel(): FinanceRecord {
        return FinanceRecord(
            id = this.id,
            month = this.month,
            date = this.createdAt ?: System.currentTimeMillis(),
            weight = this.averageDeliveryWeight ?: 0.0,
            amount = this.totalEarnings,
            type = "DELIVERY",
            year = this.year,
            totalWeight = this.totalWeight,
            pricePerKg = this.pricePerKg,
            totalEarnings = this.totalEarnings,
            deductions = this.deductions ?: 0.0,
            netEarnings = this.netEarnings ?: this.totalEarnings,
            generatedDate = this.pdfGeneratedAt ?: System.currentTimeMillis(),
            pdfUrl = this.pdfFilePath,
            transactionReference = this.serverId,
            pdfGenerated = this.pdfGenerated,
            pdfFilePath = this.pdfFilePath,
            deliveryCount = this.deliveryCount,
            paymentStatus = this.paymentStatus
        )
    }

    private fun FinanceRecordDto.toEntity(): FinanceEntity {
        val userId = preferencesManager.getUserId() ?: ""
        return FinanceEntity(
            id = this.id ?: "",
            userId = userId,
            year = this.year ?: 2024,
            month = this.month ?: 1,
            totalWeight = this.totalWeight ?: 0.0,
            pricePerKg = this.pricePerKg ?: 0.0,
            totalEarnings = this.totalEarnings ?: 0.0,
            deliveryCount = this.deliveryCount ?: 0,
            averageDeliveryWeight = this.averageDeliveryWeight ?: 0.0,
            pdfGenerated = this.pdfGenerated ?: false,
            pdfFilePath = this.pdfFilePath ?: "",
            pdfGeneratedAt = parseDateToTimestamp(this.pdfGeneratedAt),
            paymentStatus = this.paymentStatus ?: "PENDING",
            paymentDate = parseDateToTimestamp(this.paymentDate),
            deductions = this.deductions ?: 0.0,
            netEarnings = this.netEarnings ?: (this.totalEarnings ?: 0.0),
            createdAt = parseDateToTimestamp(this.createdAt) ?: System.currentTimeMillis(),
            updatedAt = parseDateToTimestamp(this.updatedAt) ?: System.currentTimeMillis(),
            syncedToServer = true,
            serverId = this.id ?: ""
        )
    }

    private suspend fun updateLocalPdfInfo(year: Int, month: Int, transcriptId: String?, pdfUrl: String?) {
        financeDao.getFinanceRecordByYearMonth(year, month)?.let { record ->
            financeDao.updatePdfInfo(
                recordId = record.id,
                pdfGenerated = true,
                pdfFilePath = pdfUrl,
                generatedAt = System.currentTimeMillis()
            )
        }
    }

    private suspend fun updateLocalPdfFilePath(year: Int, month: Int, filePath: String) {
        financeDao.getFinanceRecordByYearMonth(year, month)?.let { record ->
            financeDao.updatePdfInfo(
                recordId = record.id,
                pdfGenerated = true,
                pdfFilePath = filePath,
                generatedAt = System.currentTimeMillis()
            )
        }
    }

    private fun parseDateToTimestamp(dateString: String?): Long? {
        if (dateString == null) return null
        return try {
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            format.parse(dateString)?.time
        } catch (e: Exception) {
            null
        }
    }
}