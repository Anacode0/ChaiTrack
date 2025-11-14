package com.farmer.chaitrack.util

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppLifecycleObserver @Inject constructor(
    private val sessionManager: SessionManager
) : DefaultLifecycleObserver {

    private var isRegistered = false

    fun register() {
        if (!isRegistered) {
            ProcessLifecycleOwner.get().lifecycle.addObserver(this)
            isRegistered = true
            Log.d("AppLifecycle", "App lifecycle observer registered")
        }
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        // App came to foreground
        Log.d("AppLifecycle", "App moved to foreground")
        sessionManager.onAppForegrounded()
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        // App went to background
        Log.d("AppLifecycle", "App moved to background")
        sessionManager.onAppBackgrounded()
    }

    fun unregister() {
        if (isRegistered) {
            ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
            isRegistered = false
            Log.d("AppLifecycle", "App lifecycle observer unregistered")
        }
    }
}