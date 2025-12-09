package com.farmer.chaitrack.domain.usecase.delivery

import com.farmer.chaitrack.data.repository.DeliveryRepository
import com.farmer.chaitrack.domain.model.DeliveryItem
import com.farmer.chaitrack.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CreateDeliveryUseCase @Inject constructor(
    private val deliveryRepository: DeliveryRepository
) {
    operator fun invoke(
        weightKg: Double,
        clerkName: String,
        notes: String? = null
    ): Flow<Result<DeliveryItem>> = flow {
        emit(Result.Loading)

        val weightValidation = validateWeight(weightKg)
        if (weightValidation != null) {
            emit(Result.Error(weightValidation))
            return@flow
        }

        val clerkValidation = validateClerkName(clerkName)
        if (clerkValidation != null) {
            emit(Result.Error(clerkValidation))
            return@flow
        }

        if (notes != null) {
            val notesValidation = validateNotes(notes)
            if (notesValidation != null) {
                emit(Result.Error(notesValidation))
                return@flow
            }
        }

        // Attempt to create delivery
        /**
        try {
            val result = deliveryRepository.createDelivery(
                weightKg = weightKg,
                clerkName = clerkName.trim(),
                notes = notes?.trim()
            )
            emit(result)
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Failed to create delivery. Please try again."))
        }*/
    }

    private fun validateWeight(weight: Double): String? {
        return when {
            weight <= 0 -> "Weight must be greater than 0 kg"
            weight > 1000 -> "Weight cannot exceed 1000 kg per delivery"
            weight.toString().substringAfter('.', "").length > 2 ->
                "Weight can have at most 2 decimal places"
            else -> null
        }
    }

    private fun validateClerkName(clerkName: String): String? {
        return when {
            clerkName.isBlank() -> "Clerk name cannot be empty"
            clerkName.trim().length < 2 -> "Clerk name must be at least 2 characters"
            clerkName.trim().length > 50 -> "Clerk name cannot exceed 50 characters"
            !clerkName.matches(Regex("^[a-zA-Z\\s.'-]+$")) ->
                "Clerk name can only contain letters, spaces, dots, hyphens and apostrophes"
            else -> null
        }
    }

    private fun validateNotes(notes: String): String? {
        return when {
            notes.trim().length > 500 -> "Notes cannot exceed 500 characters"
            else -> null
        }
    }

    fun validateDeliveryRequest(
        weightKg: Double,
        clerkName: String,
        notes: String? = null
    ): String? {
        validateWeight(weightKg)?.let { return it }
        validateClerkName(clerkName)?.let { return it }
        if (notes != null) {
            validateNotes(notes)?.let { return it }
        }
        return null
    }

    fun isValidWeight(weight: Double): Boolean {
        return validateWeight(weight) == null
    }

    fun isValidClerkName(clerkName: String): Boolean {
        return validateClerkName(clerkName) == null
    }

    fun formatWeight(weight: Double): String {
        return "${"%.2f".format(weight)} kg"
    }

    fun parseWeight(weightString: String): Double? {
        return try {
            val weight = weightString.trim().toDoubleOrNull()
            if (weight != null && isValidWeight(weight)) weight else null
        } catch (e: Exception) {
            null
        }
    }
}