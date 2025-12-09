package com.farmer.chaitrack.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    fun login(phoneNumber: String, pin: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                kotlinx.coroutines.delay(1500)

                val isFirstTimeLogin = pin == phoneNumber

                if (isFirstTimeLogin) {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isLoginSuccessful = true,
                            isFirstTimeLogin = true
                        )
                    }
                } else {
                    val isValid = validateCredentials(phoneNumber, pin)

                    if (isValid) {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                isLoginSuccessful = true
                            )
                        }
                    } else {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = "Invalid phone number or PIN"
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Login failed. Please try again."
                    )
                }
            }
        }
    }

    fun recoverPin(phoneNumber: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                kotlinx.coroutines.delay(1500)

                _state.update {
                    it.copy(
                        isLoading = false,
                        isPinRecoverySent = true
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "PIN recovery failed. Please try again."
                    )
                }
            }
        }
    }

    fun resetPinRecoveryState() {
        _state.update { it.copy(isPinRecoverySent = false) }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }

    private fun validateCredentials(phoneNumber: String, pin: String): Boolean {

        return phoneNumber.length >= 10 && pin.length in 5..10
    }
}