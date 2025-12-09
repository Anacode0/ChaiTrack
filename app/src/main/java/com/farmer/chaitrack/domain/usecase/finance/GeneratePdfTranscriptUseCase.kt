package com.farmer.chaitrack.domain.usecase.finance

import com.farmer.chaitrack.domain.model.MonthlyTranscript
import com.farmer.chaitrack.domain.repository.FinanceRepository
import com.farmer.chaitrack.util.Result
import kotlinx.coroutines.flow.Flow
import java.io.File
import javax.inject.Inject

class GeneratePdfTranscriptUseCase @Inject constructor(
    private val repository: FinanceRepository
) {
    suspend operator fun invoke(year: Int, month: Int): Result<File> {
        val generateResult = repository.generateTranscript(year, month)

        return when (generateResult) {
            is Result.Success -> {
                val tempFile = File.createTempFile("transcript_${year}_${month}", ".pdf")
                Result.Success(tempFile)
            }
            is Result.Error -> Result.Error("Failed to generate transcript")
            is Result.Loading -> Result.Loading
        }
    }
}