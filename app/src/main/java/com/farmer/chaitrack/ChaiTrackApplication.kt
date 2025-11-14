package com.farmer.chaitrack

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import com.farmer.chaitrack.util.SessionManager
import com.farmer.chaitrack.BuildConfig
import com.farmer.chaitrack.util.AppLifecycleObserver
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

/**
 * ChaiTrack Application Class
 *
 * Main application class that initializes core components including:
 * - Dependency injection (Hilt)
 * - Logging (Timber)
 * - Notification channels
 * - Session management
 * - WorkManager for background tasks
 */
@HiltAndroidApp
class ChaiTrackApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var appLifecycleObserver: AppLifecycleObserver

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var sessionManager: SessionManager

    companion object {
        private const val NOTIFICATION_CHANNEL_GENERAL = "chaitrack_general"
        private const val NOTIFICATION_CHANNEL_DELIVERIES = "chaitrack_deliveries"
        private const val NOTIFICATION_CHANNEL_FERTILIZER = "chaitrack_fertilizer"
        private const val NOTIFICATION_CHANNEL_FINANCE = "chaitrack_finance"

        lateinit var instance: ChaiTrackApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        // Initialize Timber for logging
        initializeLogging()

        // Create notification channels for Android O and above
        createNotificationChannels()

        // Register lifecycle observer to handle app foreground/background
        appLifecycleObserver.register()

        // Log application start
        Timber.i("ChaiTrack Application initialized successfully")
    }

    /**
     * Initialize Timber logging based on build type
     */
    private fun initializeLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Timber.d("Timber logging initialized in DEBUG mode")
        } else {
            // In production, you might want to plant a custom tree
            // that logs to a crash reporting service like Firebase Crashlytics
            Timber.plant(ReleaseTree())
        }
    }

    /**
     * Create notification channels for different types of notifications
     * Required for Android O (API 26) and above
     */
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // General notifications channel
            val generalChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_GENERAL,
                "General Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "General announcements and updates"
                enableLights(true)
                enableVibration(true)
            }

            // Deliveries notifications channel
            val deliveriesChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_DELIVERIES,
                "Delivery Updates",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications about tea deliveries"
                enableLights(true)
                enableVibration(true)
            }

            // Fertilizer notifications channel
            val fertilizerChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_FERTILIZER,
                "Fertilizer Updates",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications about fertilizer applications"
                enableLights(true)
                enableVibration(false)
            }

            // Finance notifications channel
            val financeChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_FINANCE,
                "Finance Updates",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications about payments and transcripts"
                enableLights(true)
                enableVibration(true)
            }

            // Register all channels
            notificationManager.createNotificationChannels(
                listOf(
                    generalChannel,
                    deliveriesChannel,
                    fertilizerChannel,
                    financeChannel
                )
            )

            Timber.d("Notification channels created successfully")
        }
    }

    /**
     * Provide WorkManager configuration - Required by Configuration.Provider interface
     */
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(Log.INFO)
            .build()

    /**
     * Custom Timber tree for release builds
     * Prevents sensitive logs from being written in production
     */
    private class ReleaseTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            // Only log warnings and errors in production
            if (priority == android.util.Log.WARN || priority == android.util.Log.ERROR) {
                // Here you would send logs to your crash reporting service
                // FirebaseCrashlytics.getInstance().log(message)
            }
        }
    }

    /**
     * Clear all app data (useful for logout)
     */
    fun clearApplicationData() {
        try {
            sessionManager.clearSession()

            // Clear cache
            cacheDir?.deleteRecursively()

            // Clear WorkManager jobs
            WorkManager.getInstance(this).cancelAllWork()

            Timber.d("Application data cleared successfully")
        } catch (e: Exception) {
            Timber.e(e, "Error clearing application data")
        }
    }

    /**
     * Get application version name
     */
    fun getAppVersionName(): String {
        return try {
            packageManager.getPackageInfo(packageName, 0).versionName ?: "Unknown"
        } catch (e: Exception) {
            Timber.e(e, "Error getting version name")
            "Unknown"
        }
    }

    /**
     * Get application version code
     */
    fun getAppVersionCode(): Long {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageManager.getPackageInfo(packageName, 0).longVersionCode
            } else {
                @Suppress("DEPRECATION")
                packageManager.getPackageInfo(packageName, 0).versionCode.toLong()
            }
        } catch (e: Exception) {
            Timber.e(e, "Error getting version code")
            0L
        }
    }
}