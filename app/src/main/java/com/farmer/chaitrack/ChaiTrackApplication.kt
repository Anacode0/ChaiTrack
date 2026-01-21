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

        
        initializeLogging()

        
        createNotificationChannels()

        
        appLifecycleObserver.register()

        
        Timber.i("ChaiTrack Application initialized successfully")
    }

    
    private fun initializeLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Timber.d("Timber logging initialized in DEBUG mode")
        } else {
            
            Timber.plant(ReleaseTree())
        }
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val generalChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_GENERAL,
                "General Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "General announcements and updates"
                enableLights(true)
                enableVibration(true)
            }

            val deliveriesChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_DELIVERIES,
                "Delivery Updates",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications about tea deliveries"
                enableLights(true)
                enableVibration(true)
            }

            val fertilizerChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_FERTILIZER,
                "Fertilizer Updates",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications about fertilizer applications"
                enableLights(true)
                enableVibration(false)
            }

            val financeChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_FINANCE,
                "Finance Updates",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications about payments and transcripts"
                enableLights(true)
                enableVibration(true)
            }

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

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(Log.INFO)
            .build()

    private class ReleaseTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            
            if (priority == android.util.Log.WARN || priority == android.util.Log.ERROR) {
            }
        }
    }

    fun clearApplicationData() {
        try {
            sessionManager.clearSession()

            cacheDir?.deleteRecursively()

            WorkManager.getInstance(this).cancelAllWork()

            Timber.d("Application data cleared successfully")
        } catch (e: Exception) {
            Timber.e(e, "Error clearing application data")
        }
    }

    fun getAppVersionName(): String {
        return try {
            packageManager.getPackageInfo(packageName, 0).versionName ?: "Unknown"
        } catch (e: Exception) {
            Timber.e(e, "Error getting version name")
            "Unknown"
        }
    }

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
