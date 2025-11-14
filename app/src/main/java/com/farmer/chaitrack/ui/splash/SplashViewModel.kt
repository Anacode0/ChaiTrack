package com.farmer.chaitrack.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    private val _navigationEvent = MutableStateFlow<SplashDestination?>(null)
    val navigationEvent: StateFlow<SplashDestination?> = _navigationEvent.asStateFlow()

    fun checkAuthStatus() {
        viewModelScope.launch {
            // Simulate splash delay
            delay(2000)

            // TODO: Check if user is logged in using PreferencesManager
            val isLoggedIn = checkIfUserIsLoggedIn()

            _navigationEvent.value = if (isLoggedIn) {
                SplashDestination.MAIN
            } else {
                SplashDestination.LOGIN
            }
        }
    }

    private fun checkIfUserIsLoggedIn(): Boolean {
        // TODO: Implement actual session check
        // return PreferencesManager.isUserLoggedIn()
        return false // Default to not logged in
    }
}

enum class SplashDestination {
    LOGIN,
    MAIN
}