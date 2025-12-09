package com.farmer.chaitrack.domain.usecase.auth

import com.farmer.chaitrack.data.repository.AuthRepository
import com.farmer.chaitrack.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChangePinUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(
        currentPin: String,
        newPin: String,
        confirmNewPin: String
    ): Flow<Result<Boolean>> = flow {
        emit(Result.Loading)

        val currentPinValidation = validateCurrentPin(currentPin)
        if (currentPinValidation != null) {
            emit(Result.Error(currentPinValidation))
            return@flow
        }

        val newPinValidation = validateNewPin(newPin)
        if (newPinValidation != null) {
            emit(Result.Error(newPinValidation))
            return@flow
        }

        if (newPin != confirmNewPin) {
            emit(Result.Error("New PIN and confirmation do not match"))
            return@flow
        }

        if (currentPin == newPin) {
            emit(Result.Error("New PIN must be different from current PIN"))
            return@flow
        }

        try {
            val result = authRepository.changePin(currentPin, newPin)
            when (result) {
                is Result.Success -> emit(Result.Success(result.data))
                is Result.Error -> emit(Result.Error(result.message))
                is Result.Loading -> emit(Result.Loading)
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Failed to change PIN. Please try again."))
        }
    }

    private fun validateCurrentPin(currentPin: String): String? {
        return when {
            currentPin.isBlank() -> "Current PIN cannot be empty"
            currentPin.length < 5 -> "Current PIN must be at least 5 digits"
            !currentPin.all { it.isDigit() } -> "Current PIN must contain only numbers"
            else -> null
        }
    }

    private fun validateNewPin(newPin: String): String? {
        return when {
            newPin.isBlank() -> "New PIN cannot be empty"
            newPin.length !in 5..10 -> "New PIN must be between 5 and 10 digits"
            !newPin.all { it.isDigit() } -> "New PIN must contain only numbers"
            hasSequentialDigits(newPin) -> "New PIN should not contain sequential digits (e.g., 12345)"
            hasRepeatingDigits(newPin) -> "New PIN should not have all repeating digits (e.g., 11111)"
            else -> null
        }
    }

    private fun hasSequentialDigits(pin: String): Boolean {
        if (pin.length < 4) return false

        for (i in 0..pin.length - 4) {
            val sequence = pin.substring(i, i + 4)
            if (isSequential(sequence)) {
                return true
            }
        }
        return false
    }

    private fun isSequential(str: String): Boolean {
        for (i in 0 until str.length - 1) {
            val current = str[i].digitToIntOrNull() ?: return false
            val next = str[i + 1].digitToIntOrNull() ?: return false
            if (next - current != 1) {
                return false
            }
        }
        return true
    }

    private fun hasRepeatingDigits(pin: String): Boolean {
        if (pin.length < 4) return false
        return pin.all { it == pin[0] }
    }

    fun validatePinChangeRequest(
        currentPin: String,
        newPin: String,
        confirmNewPin: String
    ): String? {
        validateCurrentPin(currentPin)?.let { return it }
        validateNewPin(newPin)?.let { return it }

        if (newPin != confirmNewPin) {
            return "New PIN and confirmation do not match"
        }

        if (currentPin == newPin) {
            return "New PIN must be different from current PIN"
        }

        return null
    }
}