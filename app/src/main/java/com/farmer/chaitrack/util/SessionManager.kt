package com.farmer.chaitrack.util

import android.util.Log
import com.farmer.chaitrack.data.local.preferences.PreferencesManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    private val preferencesManager: PreferencesManager
) {

    companion object {
        private const val SESSION_TIMEOUT_MS = 24 * 60 * 60 * 1000L // 24 hours// Or use a shorter time like 2 hours: 2 * 60 * 60 * 1000L
    }

    /**
     * Check if user is logged in
     */
    fun isLoggedIn(): Boolean {
        return preferencesManager.isLoggedIn()
    }

    /**
     * Update last activity time - call this during user interactions
     */
    fun updateActivity() {
        preferencesManager.updateLastActivityTime()
    }

    /**
     * Check if session is still valid based on last activity
     */
    fun isSessionValid(): Boolean {
        if (!preferencesManager.isLoggedIn()) {
            return false
        }

        val lastActivityTime = preferencesManager.getLastActivityTime()
        val currentTime = System.currentTimeMillis()
        val timeSinceLastActivity = currentTime - lastActivityTime

        return timeSinceLastActivity < SESSION_TIMEOUT_MS
    }

    /**
     * Invalidate current session
     */
    fun invalidateSession() {
        preferencesManager.setLoggedIn(false)
    }

    /**
     * Clear all session data
     */
    fun clearSession() {
        preferencesManager.clearUserData()
    }

    /**
     * Check if app should redirect to login
     */
    fun shouldRedirectToLogin(): Boolean {
        return !preferencesManager.isLoggedIn() || !isSessionValid()
    }

    /**
     * Call when app comes to foreground to refresh session if still valid
     */
    fun onAppForegrounded() {
        Log.d("SessionManager", "App foregrounded - refreshing data")
        if (isLoggedIn() && isSessionValid()) {
            // Session is still valid, update the activity time
            updateActivity()
        }
    }

    /**
     * Call when app goes to background
     */
    fun onAppBackgrounded() {
        Log.d("SessionManager", "App backgrounded - saving state")
        // Update last activity time when going to background
        if (isLoggedIn()) {
            updateActivity()
        }
    }
}