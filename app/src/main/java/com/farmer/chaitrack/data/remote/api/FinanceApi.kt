package com.farmer.chaitrack.data.remote.api

import com.farmer.chaitrack.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface FinanceApi {

    @GET("finance/records")
    suspend fun getFinanceRecords(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 50
    ): Response<FinanceRecordsResponse>

    @GET("finance/records/{id}")
    suspend fun getFinanceRecordById(
        @Header("Authorization") token: String,
        @Path("id") recordId: String
    ): Response<FinanceRecordDetailResponse>

    @GET("finance/records/month")
    suspend fun getFinanceRecordByMonth(
        @Header("Authorization") token: String,
        @Query("year") year: Int,
        @Query("month") month: Int // 1-12
    ): Response<FinanceRecordDetailResponse>

    @GET("finance/records/year")
    suspend fun getFinanceRecordsByYear(
        @Header("Authorization") token: String,
        @Query("year") year: Int
    ): Response<FinanceRecordsResponse>

    @GET("finance/summary")
    suspend fun getFinancialSummary(
        @Header("Authorization") token: String,
        @Query("startYear") startYear: Int? = null,
        @Query("endYear") endYear: Int? = null
    ): Response<FinancialSummaryResponse>

    @GET("finance/earnings/yearly")
    suspend fun getYearlyEarnings(
        @Header("Authorization") token: String,
        @Query("year") year: Int
    ): Response<YearlyEarningsResponse>

    @GET("finance/earnings/monthly")
    suspend fun getMonthlyEarnings(
        @Header("Authorization") token: String,
        @Query("year") year: Int,
        @Query("month") month: Int
    ): Response<MonthlyEarningsResponse>

    @POST("finance/transcript/generate")
    suspend fun generateTranscript(
        @Header("Authorization") token: String,
        @Body request: GenerateTranscriptRequest
    ): Response<GenerateTranscriptResponse>

    @GET("finance/transcript/{id}/download")
    @Streaming
    suspend fun downloadTranscript(
        @Header("Authorization") token: String,
        @Path("id") transcriptId: String
    ): Response<okhttp3.ResponseBody>

    @GET("finance/transcript/download")
    @Streaming
    suspend fun downloadTranscriptByMonth(
        @Header("Authorization") token: String,
        @Query("year") year: Int,
        @Query("month") month: Int
    ): Response<okhttp3.ResponseBody>

    @POST("finance/transcript/{id}/share")
    suspend fun shareTranscript(
        @Header("Authorization") token: String,
        @Path("id") transcriptId: String,
        @Body request: ShareTranscriptRequest
    ): Response<ShareTranscriptResponse>

    @GET("finance/transcript/history")
    suspend fun getTranscriptHistory(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Response<TranscriptHistoryResponse>

    @GET("finance/price")
    suspend fun getPriceForPeriod(
        @Header("Authorization") token: String,
        @Query("year") year: Int,
        @Query("month") month: Int
    ): Response<PriceResponse>

    @GET("finance/price/history")
    suspend fun getPriceHistory(
        @Header("Authorization") token: String,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null
    ): Response<PriceHistoryResponse>

    @GET("finance/payments")
    suspend fun getPaymentHistory(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 50
    ): Response<PaymentHistoryResponse>

    @GET("finance/payments/{id}")
    suspend fun getPaymentById(
        @Header("Authorization") token: String,
        @Path("id") paymentId: String
    ): Response<PaymentDetailResponse>

    /** @GET("finance/bank-account")
    suspend fun getBankAccount(
        @Header("Authorization") token: String
    ): Response<BankAccountResponse>
     */

    @PUT("finance/bank-account")
    suspend fun updateBankAccount(
        @Header("Authorization") token: String,
        @Body request: UpdateBankAccountRequest
    ): Response<UpdateBankAccountResponse>

    @GET("finance/deductions")
    suspend fun getDeductions(
        @Header("Authorization") token: String,
        @Query("year") year: Int,
        @Query("month") month: Int
    ): Response<DeductionsResponse>

    @GET("finance/export/csv")
    @Streaming
    suspend fun exportFinanceRecordsToCsv(
        @Header("Authorization") token: String,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null
    ): Response<okhttp3.ResponseBody>

    @GET("finance/export/pdf")
    @Streaming
    suspend fun exportFinanceRecordsToPdf(
        @Header("Authorization") token: String,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null
    ): Response<okhttp3.ResponseBody>

    @GET("finance/tax")
    suspend fun getTaxInformation(
        @Header("Authorization") token: String,
        @Query("year") year: Int
    ): Response<TaxInformationResponse>

    @POST("finance/report-issue")
    suspend fun reportFinancialIssue(
        @Header("Authorization") token: String,
        @Body request: ReportIssueRequest
    ): Response<ReportIssueResponse>

    /**@POST("finance/transcript/verify")
    suspend fun verifyTranscript(
        @Body request: VerifyTranscriptRequest
    ): Response<VerifyTranscriptResponse>
    */
}