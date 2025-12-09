package com.farmer.chaitrack.ui.auth

data class LoginState(
    val isLoading: Boolean = false,
    val isLoginSuccessful: Boolean = false,
    val isFirstTimeLogin: Boolean = false,
    val isPinRecoverySent: Boolean = false,
    val error: String? = null,
    val userPhoneNumber: String? = null
)