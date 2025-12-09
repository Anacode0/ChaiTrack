package com.farmer.chaitrack.domain.usecase.auth

import com.farmer.chaitrack.data.repository.AuthRepository
import com.farmer.chaitrack.domain.model.BankDetails
import com.farmer.chaitrack.domain.model.User
import com.farmer.chaitrack.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(
        phoneNumber: String,
        pin: String
    ): Flow<Result<User>> = flow {
        emit(Result.Loading)

        val phoneValidation = validatePhoneNumber(phoneNumber)
        if (phoneValidation != null) {
            emit(Result.Error(phoneValidation))
            return@flow
        }

        val pinValidation = validatePin(pin)
        if (pinValidation != null) {
            emit(Result.Error(pinValidation))
            return@flow
        }

        val cleanedPhone = cleanPhoneNumber(phoneNumber)

        try {
            val result = authRepository.login(cleanedPhone, pin)
            when (result) {
                is Result.Success -> {
                    // Convert LoginData to User domain model
                    val user = mapLoginDataToUser(result.data, pin)
                    emit(Result.Success(user))
                }
                is Result.Error -> {
                    emit(Result.Error(result.message))
                }
                is Result.Loading -> {
                }
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Login failed. Please check your credentials and try again."))
        }
    }

    private fun mapLoginDataToUser(
        loginData: com.farmer.chaitrack.data.remote.dto.LoginData,
        userPin: String
    ): User {
        val userDto = loginData.user
        val currentTime = System.currentTimeMillis()

        return User(
            id = userDto.id,
            phoneNumber = userDto.phoneNumber,
            name = userDto.name,
            email = getEmailFromDto(userDto),
            plotSize = userDto.plotSize,
            farmName = userDto.farmName,
            collectionCenter = userDto.collectionCenter,
            profileImageUrl = userDto.profileImageUrl,
            firstLoginYear = userDto.firstLoginYear,
            pin = userPin,
            plotSizeAcres = userDto.plotSize ?: 0.0,
            farmerCode = generateFarmerCode(userDto.name, userDto.phoneNumber),
            registrationDate = currentTime,
            lastLoginDate = currentTime,
            isActive = true,
            bankDetails = null
        )
    }

    private fun getEmailFromDto(userDto: com.farmer.chaitrack.data.remote.dto.UserDto): String? {
        return try {
            userDto.email
        } catch (e: NoSuchFieldError) {
            null
        } catch (e: Exception) {
            null
        }
    }

    private fun generateFarmerCode(name: String, phoneNumber: String): String {
        val nameInitials = name.split(" ")
            .take(2)
            .joinToString("") { it.take(1).uppercase() }
        val phoneSuffix = phoneNumber.takeLast(4)
        return "FM${nameInitials}$phoneSuffix"
    }

    private fun validatePhoneNumber(phoneNumber: String): String? {
        return when {
            phoneNumber.isBlank() -> "Phone number cannot be empty"
            !phoneNumber.matches(Regex("^[0-9+\\s()-]+$")) -> "Phone number can only contain digits and +, -, (), spaces"
            else -> {
                val digitsOnly = phoneNumber.filter { it.isDigit() }
                when {
                    digitsOnly.length < 9 -> "Phone number must be at least 9 digits"
                    digitsOnly.length > 12 -> "Phone number cannot exceed 12 digits"
                    else -> null
                }
            }
        }
    }

    private fun validatePin(pin: String): String? {
        return when {
            pin.isBlank() -> "PIN cannot be empty"
            pin.length < 5 -> "PIN must be at least 5 digits"
            pin.length > 10 -> "PIN cannot exceed 10 digits"
            !pin.all { it.isDigit() } -> "PIN must contain only numbers"
            else -> null
        }
    }

    private fun cleanPhoneNumber(phoneNumber: String): String {
        var cleaned = phoneNumber.replace(Regex("[^0-9+]"), "")

        when {
            cleaned.startsWith("+254") -> {
                cleaned = cleaned.removePrefix("+")
            }
            cleaned.startsWith("0") && cleaned.length == 10 -> {
                cleaned = "254${cleaned.substring(1)}"
            }
            cleaned.length == 9 -> {
                cleaned = "254$cleaned"
            }
        }

        return cleaned
    }

    fun validateCredentials(phoneNumber: String, pin: String): String? {
        validatePhoneNumber(phoneNumber)?.let { return it }
        validatePin(pin)?.let { return it }
        return null
    }

    fun isValidPhoneNumber(phoneNumber: String): Boolean {
        return validatePhoneNumber(phoneNumber) == null
    }

    fun isValidPin(pin: String): Boolean {
        return validatePin(pin) == null
    }

    fun formatPhoneNumberForDisplay(phoneNumber: String): String {
        val digitsOnly = phoneNumber.filter { it.isDigit() }

        return when {
            digitsOnly.length == 10 && digitsOnly.startsWith("0") -> {
                "${digitsOnly.substring(0, 4)} ${digitsOnly.substring(4, 7)} ${digitsOnly.substring(7)}"
            }
            digitsOnly.length == 12 && digitsOnly.startsWith("254") -> {
                "+254 ${digitsOnly.substring(3, 6)} ${digitsOnly.substring(6, 9)} ${digitsOnly.substring(9)}"
            }
            digitsOnly.length == 9 -> {
                "${digitsOnly.substring(0, 3)} ${digitsOnly.substring(3, 6)} ${digitsOnly.substring(6)}"
            }
            else -> phoneNumber
        }
    }
}