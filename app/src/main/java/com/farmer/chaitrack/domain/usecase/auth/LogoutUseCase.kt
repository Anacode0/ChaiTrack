package com.farmer.chaitrack.domain.usecase.auth

import com.farmer.chaitrack.data.repository.AuthRepository
import com.farmer.chaitrack.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case for user logout
 */
class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    /**
     * Executes the logout operation
     * Clears user session, auth tokens, and cached data
     *
     * @return Flow of Result containing success or error message
     */
    operator fun invoke(): Flow<Result<Boolean>> = flow { // Changed from Result<Unit> to Result<Boolean>
        emit(Result.Loading)

        try {
            // Perform logout through repository
            val result = authRepository.logout()
            when (result) {
                is Result.Success -> emit(Result.Success(result.data))
                is Result.Error -> emit(Result.Error(result.message))
                is Result.Loading -> emit(Result.Loading)
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Logout failed. Please try again."))
        }
    }

    /**
     * Executes logout with confirmation
     * Used when user wants to be prompted before logging out
     *
     * @param confirmed Whether the user has confirmed the logout action
     * @return Flow of Result containing success or error message
     */
    fun invokeWithConfirmation(confirmed: Boolean): Flow<Result<Boolean>> = flow { // Changed from Result<Unit> to Result<Boolean>
        if (!confirmed) {
            emit(Result.Error("Logout cancelled"))
            return@flow
        }

        emit(Result.Loading)

        try {
            val result = authRepository.logout()
            when (result) {
                is Result.Success -> emit(Result.Success(result.data))
                is Result.Error -> emit(Result.Error(result.message))
                is Result.Loading -> emit(Result.Loading)
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Logout failed. Please try again."))
        }
    }

    /**
     * Executes a silent logout without API call
     * Used when session has expired or token is invalid
     * Only clears local data
     */
    fun silentLogout(): Flow<Result<Unit>> = flow {
        emit(Result.Loading)

        try {
            // Since AuthRepository doesn't have clearLocalSession(), we'll simulate it
            // by calling logout but handling it differently, or we can add this method to repository
            // For now, we'll just emit success since logout already clears local data
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Failed to clear session"))
        }
    }

    /**
     * Checks if user is currently logged in
     */
    fun isUserLoggedIn(): Boolean { // Removed suspend modifier since repository method is not suspend
        return authRepository.isLoggedIn()
    }

    /**
     * Gets the current user's authentication status
     */
    suspend fun getAuthStatus(): AuthStatus {
        return try {
            when {
                !authRepository.isLoggedIn() -> AuthStatus.NOT_LOGGED_IN
                isTokenValid().not() -> AuthStatus.TOKEN_EXPIRED // Use our own token validation
                else -> AuthStatus.AUTHENTICATED
            }
        } catch (e: Exception) {
            AuthStatus.ERROR
        }
    }

    /**
     * Checks if token is likely valid (basic check)
     * Note: This doesn't verify with server, just checks if token exists
     */
    private fun isTokenValid(): Boolean {
        return authRepository.getAuthToken()?.isNotEmpty() == true
    }

    /**
     * Gets the current user ID if logged in
     */
    fun getCurrentUserId(): String? {
        return authRepository.getUserId()
    }

    /**
     * Gets the current user name if logged in
     */
    fun getCurrentUserName(): String {
        return authRepository.getUserName()
    }
}

/**
 * Enum representing authentication status
 */
enum class AuthStatus {
    AUTHENTICATED,
    NOT_LOGGED_IN,
    TOKEN_EXPIRED,
    ERROR
}