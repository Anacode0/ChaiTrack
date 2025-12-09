package com.farmer.chaitrack.domain.usecase.fertilizer

import com.farmer.chaitrack.domain.model.FertilizerApplication
import javax.inject.Inject

class ValidateFertilizerApplicationUseCase @Inject constructor() {

    data class ValidationResult(
        val isValid: Boolean,
        val errorMessage: String? = null
    )

    operator fun invoke(application: FertilizerApplication): ValidationResult {
        return when {
            application.season.isBlank() -> ValidationResult(false, "Season is required")
            application.plotSize <= 0 -> ValidationResult(false, "Plot size must be greater than 0")
            application.quantityRequested <= 0 -> ValidationResult(false, "Quantity must be greater than 0")
            application.quantityRequested > 50 -> ValidationResult(false, "Quantity cannot exceed 50 bags")
            else -> ValidationResult(true)
        }
    }
}