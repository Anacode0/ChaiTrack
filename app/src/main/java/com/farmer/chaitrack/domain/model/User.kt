package com.farmer.chaitrack.domain.model

data class User(
    val id: String,
    val phoneNumber: String,
    val name: String,
    val email: String?,
    val plotSize: Double? = null,
    val farmName: String? = null,
    val collectionCenter: String? = null,
    val profileImageUrl: String? = null,
    val firstLoginYear: Int? = null,
    val pin: String,
    val plotSizeAcres: Double,
    val farmerCode: String,
    val registrationDate: Long,
    val lastLoginDate: Long?,
    val isActive: Boolean,
    val bankDetails: BankDetails?
) {
    fun getDisplayName(): String {
        return if (name.contains(" ")) {
            name.split(" ").first()
        } else {
            name
        }
    }

    fun getWelcomeMessage(): String {
        return "Welcome ${getDisplayName()}"
    }

    fun getFormattedPhoneNumber(): String {
        return if (phoneNumber.length == 12 && phoneNumber.startsWith("254")) {
            "+254 ${phoneNumber.substring(3, 6)} ${phoneNumber.substring(6, 9)} ${phoneNumber.substring(9)}"
        } else if (phoneNumber.length == 10 && phoneNumber.startsWith("0")) {
            phoneNumber.replaceFirst("0", "+254 ")
                .chunked(3)
                .joinToString(" ")
        } else {
            phoneNumber
        }
    }

    fun getInitials(): String {
        val nameParts = name.trim().split(" ")
        return when {
            nameParts.size >= 2 -> {
                "${nameParts[0].first().uppercaseChar()}${nameParts[1].first().uppercaseChar()}"
            }
            nameParts.isNotEmpty() -> {
                nameParts[0].take(2).uppercase()
            }
            else -> "??"
        }
    }

    fun hasProfileImage(): Boolean {
        return !profileImageUrl.isNullOrEmpty()
    }

    fun hasBankDetails(): Boolean {
        return bankDetails != null && bankDetails.isComplete()
    }

    fun getFormattedPlotSize(): String {
        return "${"%.1f".format(plotSizeAcres)} acres"
    }

    fun isFirstLogin(): Boolean {
        return lastLoginDate == null
    }

    fun needsPinChange(): Boolean {
        return pin == phoneNumber || pin == phoneNumber.takeLast(10)
    }
}

data class BankDetails(
    val bankName: String?,
    val accountNumber: String?,
    val accountName: String?,
    val branchName: String?
) {
    fun isComplete(): Boolean {
        return !bankName.isNullOrEmpty() &&
                !accountNumber.isNullOrEmpty() &&
                !accountName.isNullOrEmpty()
    }

    fun getMaskedAccountNumber(): String {
        return if (!accountNumber.isNullOrEmpty() && accountNumber.length > 4) {
            val lastFour = accountNumber.takeLast(4)
            val masked = "*".repeat(accountNumber.length - 4)
            "$masked$lastFour"
        } else {
            accountNumber ?: ""
        }
    }

    fun getDisplayText(): String {
        return if (isComplete()) {
            "$bankName - ${getMaskedAccountNumber()}"
        } else {
            "No bank details"
        }
    }
}

enum class UserRole {
    FARMER,
    CLERK,
    ADMIN,
    SUPPORT;

    companion object {
        fun fromString(value: String): UserRole {
            return values().find {
                it.name.equals(value, ignoreCase = true)
            } ?: FARMER
        }
    }
}

data class UserCredentials(
    val phoneNumber: String,
    val pin: String
) {
    fun isValidPhoneNumber(): Boolean {
        val cleanNumber = phoneNumber.replace(Regex("[^0-9]"), "")
        return cleanNumber.length in 9..12
    }

    fun isValidPin(): Boolean {
        return pin.length in 5..10 && pin.all { it.isDigit() }
    }

    fun isValid(): Boolean {
        return isValidPhoneNumber() && isValidPin()
    }
}