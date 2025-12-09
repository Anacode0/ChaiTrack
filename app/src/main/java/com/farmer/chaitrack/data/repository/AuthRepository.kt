package com.farmer.chaitrack.data.repository

import com.farmer.chaitrack.data.local.preferences.PreferencesManager
import com.farmer.chaitrack.data.remote.api.AuthApi
import com.farmer.chaitrack.data.remote.dto.*
import com.farmer.chaitrack.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar

class AuthRepository(
    private val authApi: AuthApi,
    private val preferencesManager: PreferencesManager
) {
    suspend fun login(phoneNumber: String, pin: String, fcmToken: String? = null): Result<LoginData> {
        return withContext(Dispatchers.IO) {
            try {
                val request = LoginRequest(
                    phoneNumber = phoneNumber,
                    pin = pin,
                    fcmToken = fcmToken
                )

                val response = authApi.login(request)

                if (response.isSuccessful && response.body()?.success == true) {
                    val loginData = response.body()?.data
                    if (loginData != null) {
                        saveAuthData(loginData)

                        saveUserData(loginData.user)

                        Result.Success(loginData)
                    } else {
                        Result.Error("Invalid response data")
                    }
                } else {
                    val errorMessage = response.body()?.message ?: "Login failed"
                    Result.Error(errorMessage)
                }
            } catch (e: Exception) {
                Result.Error(e.message ?: "Network error occurred")
            }
        }
    }

    suspend fun logout(): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val token = preferencesManager.getAuthToken()

                if (token != null) {
                    authApi.logout("Bearer $token")
                }

                clearAuthData()

                Result.Success(true)
            } catch (e: Exception) {
                clearAuthData()
                Result.Success(true)
            }
        }
    }

    suspend fun refreshToken(): Result<RefreshTokenResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val refreshToken = preferencesManager.getRefreshToken()
                    ?: return@withContext Result.Error("No refresh token available")

                val request = RefreshTokenRequest(refreshToken = refreshToken)
                val response = authApi.refreshToken(request)

                if (response.isSuccessful && response.body()?.success == true) {
                    val data = response.body()?.data
                    if (data != null) {
                        // Save new tokens
                        preferencesManager.saveAuthToken(data.authToken)
                        preferencesManager.saveRefreshToken(data.refreshToken)

                        Result.Success(response.body()!!)
                    } else {
                        Result.Error("Invalid refresh response")
                    }
                } else {
                    Result.Error("Token refresh failed")
                }
            } catch (e: Exception) {
                Result.Error(e.message ?: "Network error occurred")
            }
        }
    }

    suspend fun verifyToken(): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val token = preferencesManager.getAuthToken()
                    ?: return@withContext Result.Error("No auth token")

                val response = authApi.verifyToken("Bearer $token")

                if (response.isSuccessful && response.body()?.success == true) {
                    Result.Success(true)
                } else {
                    Result.Error("Token is invalid")
                }
            } catch (e: Exception) {
                Result.Error(e.message ?: "Network error occurred")
            }
        }
    }

    suspend fun changePin(currentPin: String, newPin: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val token = preferencesManager.getAuthToken()
                    ?: return@withContext Result.Error("Not authenticated")

                val request = ChangePinRequest(
                    currentPin = currentPin,
                    newPin = newPin
                )

                val response = authApi.changePin("Bearer $token", request)

                if (response.isSuccessful && response.body()?.success == true) {
                    Result.Success(true)
                } else {
                    val errorMessage = response.body()?.message ?: "Failed to change PIN"
                    Result.Error(errorMessage)
                }
            } catch (e: Exception) {
                Result.Error(e.message ?: "Network error occurred")
            }
        }
    }

    suspend fun forgotPin(phoneNumber: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val request = ForgotPinRequest(phoneNumber = phoneNumber)
                val response = authApi.forgotPin(request)

                if (response.isSuccessful && response.body()?.success == true) {
                    Result.Success(true)
                } else {
                    val errorMessage = response.body()?.message ?: "Failed to send recovery code"
                    Result.Error(errorMessage)
                }
            } catch (e: Exception) {
                Result.Error(e.message ?: "Network error occurred")
            }
        }
    }

    suspend fun getUserProfileWithStats(): Result<UserProfileData> {
        return withContext(Dispatchers.IO) {
            try {
                val token = preferencesManager.getAuthToken()
                    ?: return@withContext Result.Error("Not authenticated")

                val response = authApi.getProfile("Bearer $token")

                if (response.isSuccessful && response.body()?.success == true) {
                    val userProfileData = response.body()?.data
                    if (userProfileData != null && userProfileData.user != null) {
                        saveUserData(userProfileData.user)

                        Result.Success(userProfileData)
                    } else {
                        Result.Error("Invalid profile data")
                    }
                } else {
                    Result.Error("Failed to fetch profile")
                }
            } catch (e: Exception) {
                Result.Error(e.message ?: "Network error occurred")
            }
        }
    }
    fun isLoggedIn(): Boolean {
        return preferencesManager.isLoggedIn()
    }

    fun getUserId(): String? {
        return preferencesManager.getUserId()
    }

    fun getUserName(): String {
        return preferencesManager.getUserName()
    }

    fun getAuthToken(): String? {
        return preferencesManager.getAuthToken()
    }

    private fun saveAuthData(loginData: LoginData) {
        preferencesManager.apply {
            setLoggedIn(true)
            saveAuthToken(loginData.authToken)
            saveRefreshToken(loginData.refreshToken)
            updateLastActivityTime()
        }
    }

    private fun saveUserData(user: UserDto) {
        preferencesManager.apply {
            saveUserData(user.id, user.name, user.phoneNumber)

            user.plotSize?.let { setPlotSize(it) }
            user.farmName?.let { setFarmName(it) }
            user.collectionCenter?.let { setCollectionCenter(it) }
            user.profileImageUrl?.let { setProfileImageUrl(it) }

            val firstYear = user.firstLoginYear ?: Calendar.getInstance().get(Calendar.YEAR)
            setFirstLoginYear(firstYear)
        }
    }

    private fun clearAuthData() {
        preferencesManager.clearUserData()
    }
}