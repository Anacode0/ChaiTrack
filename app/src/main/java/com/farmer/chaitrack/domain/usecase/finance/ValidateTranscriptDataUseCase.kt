package com.farmer.chaitrack.domain.usecase.finance

import com.farmer.chaitrack.domain.model.MonthlyTranscript
import javax.inject.Inject

class ValidateTranscriptDataUseCase @Inject constructor() {

    data class ValidationResult(
        val isValid: Boolean,
        val errorMessage: String? = null,
        val warnings: List<String> = emptyList()
    )

    operator fun invoke(transcript: MonthlyTranscript): ValidationResult {
        val errors = mutableListOf<String>()
        val warnings = mutableListOf<String>()

        if (transcript.month !in 1..12) {
            errors.add("Invalid month: ${transcript.month}. Must be between 1 and 12.")
        }

        if (transcript.year < 2020 || transcript.year > 2100) {
            errors.add("Invalid year: ${transcript.year}. Must be between 2020 and 2100.")
        }

        if (transcript.totalWeight <= 0) {
            errors.add("Total weight must be positive. Current: ${transcript.totalWeight}")
        }

        if (transcript.pricePerKg <= 0) {
            errors.add("Price per kg must be positive. Current: ${transcript.pricePerKg}")
        }

        if (transcript.totalEarnings < 0) {
            errors.add("Total earnings cannot be negative. Current: ${transcript.totalEarnings}")
        }

        if (transcript.totalWeight > 10000) {
            warnings.add("Total weight seems unusually high: ${transcript.totalWeight} kg")
        }

        if (transcript.pricePerKg > 1000) {
            warnings.add("Price per kg seems unusually high: ${transcript.pricePerKg}")
        }

        if (transcript.totalEarnings > 10000000) {
            warnings.add("Total earnings seem unusually high: ${transcript.totalEarnings}")
        }

        val expectedEarnings = transcript.calculateExpectedEarnings()
        val earningsDifference = transcript.totalEarnings - expectedEarnings
        val tolerance = 0.01

        if (Math.abs(earningsDifference) > tolerance) {
            warnings.add("Recorded earnings (${transcript.totalEarnings}) don't match calculated earnings ($expectedEarnings). Difference: $earningsDifference")
        }

        return if (errors.isEmpty()) {
            ValidationResult(
                isValid = true,
                warnings = warnings
            )
        } else {
            ValidationResult(
                isValid = false,
                errorMessage = errors.joinToString("\n"),
                warnings = warnings
            )
        }
    }

    fun validateMultiple(transcripts: List<MonthlyTranscript>): List<ValidationResult> {
        return transcripts.map { invoke(it) }
    }

    fun findDuplicates(transcripts: List<MonthlyTranscript>): List<MonthlyTranscript> {
        val seen = mutableSetOf<Pair<Int, Int>>() // Pair of (month, year)
        val duplicates = mutableListOf<MonthlyTranscript>()

        transcripts.forEach { transcript ->
            val key = Pair(transcript.month, transcript.year)
            if (seen.contains(key)) {
                duplicates.add(transcript)
            } else {
                seen.add(key)
            }
        }

        return duplicates
    }

    fun validateChronologicalOrder(transcripts: List<MonthlyTranscript>): ValidationResult {
        if (transcripts.size <= 1) {
            return ValidationResult(true)
        }

        val sorted = transcripts.sortedWith(compareBy({ it.year }, { it.month }))
        val isChronological = transcripts == sorted

        return if (isChronological) {
            ValidationResult(true)
        } else {
            ValidationResult(false, "Transcripts are not in chronological order")
        }
    }
}