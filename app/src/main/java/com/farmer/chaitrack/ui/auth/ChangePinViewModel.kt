package com.farmer.chaitrack.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChangePinViewModel : ViewModel() {

    private val _state = MutableStateFlow(ChangePinState())
    val state: StateFlow<ChangePinState> = _state.asStateFlow()

    fun changePin(currentPin: String, newPin: String, confirmPin: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                if (!validateInputs(currentPin, newPin, confirmPin)) {
                    return@launch
                }

                kotlinx.coroutines.delay(1000)
                val success = changePinInRepository(currentPin, newPin)

                if (success) {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = "Current PIN is incorrect"
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to change PIN"
                    )
                }
            }
        }
    }

    private fun validateInputs(currentPin: String, newPin: String, confirmPin: String): Boolean {
        when {
            currentPin.isEmpty() -> {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Please enter your current PIN"
                    )
                }
                return false
            }
            newPin.isEmpty() -> {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Please enter a new PIN"
                    )
                }
                return false
            }
            newPin.length < 5 || newPin.length > 10 -> {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "PIN must be between 5 and 10 digits"
                    )
                }
                return false
            }
            newPin != confirmPin -> {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "PINs do not match"
                    )
                }
                return false
            }
            newPin == currentPin -> {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "New PIN must be different from current PIN"
                    )
                }
                return false
            }
        }
        return true
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }

    fun resetState() {
        _state.update { it.copy(isSuccess = false, error = null) }
    }

    private fun changePinInRepository(currentPin: String, newPin: String): Boolean {
        return true
    }
}

data class ChangePinState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)