package com.farmer.chaitrack.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import timber.log.Timber
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Utility class for network-related operations
 * Provides network connectivity checking and monitoring
 */
@Singleton
class NetworkUtils @Inject constructor(
    private val context: Context
) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    companion object {
        private const val PING_HOST = "8.8.8.8" // Google DNS
        private const val PING_PORT = 53
        private const val PING_TIMEOUT = 1500 // milliseconds
    }

    /**
     * Check if device is connected to the internet
     * Works on all Android versions
     */
    fun isNetworkAvailable(): Boolean {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network = connectivityManager.activeNetwork ?: return false
                val capabilities = connectivityManager.getNetworkCapabilities(network)
                    ?: return false

                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                        capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            } else {
                @Suppress("DEPRECATION")
                val networkInfo = connectivityManager.activeNetworkInfo
                @Suppress("DEPRECATION")
                networkInfo != null && networkInfo.isConnected
            }
        } catch (e: Exception) {
            Timber.e(e, "Error checking network availability")
            false
        }
    }

    /**
     * Check if connected to WiFi
     */
    fun isWifiConnected(): Boolean {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network = connectivityManager.activeNetwork ?: return false
                val capabilities = connectivityManager.getNetworkCapabilities(network)
                    ?: return false
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            } else {
                @Suppress("DEPRECATION")
                val networkInfo = connectivityManager.activeNetworkInfo
                @Suppress("DEPRECATION")
                networkInfo?.type == ConnectivityManager.TYPE_WIFI &&
                        networkInfo.isConnected
            }
        } catch (e: Exception) {
            Timber.e(e, "Error checking WiFi connectivity")
            false
        }
    }

    /**
     * Check if connected to mobile data
     */
    fun isMobileDataConnected(): Boolean {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network = connectivityManager.activeNetwork ?: return false
                val capabilities = connectivityManager.getNetworkCapabilities(network)
                    ?: return false
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
            } else {
                @Suppress("DEPRECATION")
                val networkInfo = connectivityManager.activeNetworkInfo
                @Suppress("DEPRECATION")
                networkInfo?.type == ConnectivityManager.TYPE_MOBILE &&
                        networkInfo.isConnected
            }
        } catch (e: Exception) {
            Timber.e(e, "Error checking mobile data connectivity")
            false
        }
    }

    /**
     * Get the type of network connection
     */
    fun getNetworkType(): NetworkType {
        return try {
            if (!isNetworkAvailable()) {
                return NetworkType.NONE
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network = connectivityManager.activeNetwork ?: return NetworkType.NONE
                val capabilities = connectivityManager.getNetworkCapabilities(network)
                    ?: return NetworkType.NONE

                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ->
                        NetworkType.WIFI
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ->
                        NetworkType.MOBILE
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ->
                        NetworkType.ETHERNET
                    else -> NetworkType.OTHER
                }
            } else {
                @Suppress("DEPRECATION")
                val networkInfo = connectivityManager.activeNetworkInfo
                @Suppress("DEPRECATION")
                when (networkInfo?.type) {
                    ConnectivityManager.TYPE_WIFI -> NetworkType.WIFI
                    ConnectivityManager.TYPE_MOBILE -> NetworkType.MOBILE
                    ConnectivityManager.TYPE_ETHERNET -> NetworkType.ETHERNET
                    else -> NetworkType.OTHER
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "Error getting network type")
            NetworkType.NONE
        }
    }

    /**
     * Observe network connectivity changes as a Flow
     * Emits true when connected, false when disconnected
     */
    fun observeNetworkConnectivity(): Flow<Boolean> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            private val networks = mutableSetOf<Network>()

            override fun onAvailable(network: Network) {
                networks.add(network)
                trySend(true)
                Timber.d("Network available: $network")
            }

            override fun onLost(network: Network) {
                networks.remove(network)
                trySend(networks.isNotEmpty())
                Timber.d("Network lost: $network")
            }

            override fun onCapabilitiesChanged(
                network: Network,
                capabilities: NetworkCapabilities
            ) {
                val hasInternet = capabilities.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_INTERNET
                )
                val isValidated = capabilities.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_VALIDATED
                )

                if (hasInternet && isValidated) {
                    networks.add(network)
                } else {
                    networks.remove(network)
                }
                trySend(networks.isNotEmpty())
            }
        }

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, callback)

        // Send initial state
        trySend(isNetworkAvailable())

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
            Timber.d("Network callback unregistered")
        }
    }.distinctUntilChanged()

    /**
     * Ping a host to check actual internet connectivity
     * This is more reliable than just checking network state
     */
    suspend fun hasInternetAccess(): Boolean {
        return try {
            val socket = Socket()
            socket.connect(InetSocketAddress(PING_HOST, PING_PORT), PING_TIMEOUT)
            socket.close()
            true
        } catch (e: IOException) {
            Timber.w("Internet access check failed: ${e.message}")
            false
        } catch (e: Exception) {
            Timber.e(e, "Error checking internet access")
            false
        }
    }

    /**
     * Get network speed/quality indicator
     */
    fun getNetworkQuality(): NetworkQuality {
        return try {
            if (!isNetworkAvailable()) {
                return NetworkQuality.NO_CONNECTION
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network = connectivityManager.activeNetwork ?: return NetworkQuality.NO_CONNECTION
                val capabilities = connectivityManager.getNetworkCapabilities(network)
                    ?: return NetworkQuality.NO_CONNECTION

                val downSpeed = capabilities.linkDownstreamBandwidthKbps

                when {
                    downSpeed >= 5000 -> NetworkQuality.EXCELLENT
                    downSpeed >= 2000 -> NetworkQuality.GOOD
                    downSpeed >= 1000 -> NetworkQuality.FAIR
                    downSpeed > 0 -> NetworkQuality.POOR
                    else -> NetworkQuality.UNKNOWN
                }
            } else {
                // For older Android versions, we can only check connection type
                when (getNetworkType()) {
                    NetworkType.WIFI, NetworkType.ETHERNET -> NetworkQuality.GOOD
                    NetworkType.MOBILE -> NetworkQuality.FAIR
                    else -> NetworkQuality.UNKNOWN
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "Error getting network quality")
            NetworkQuality.UNKNOWN
        }
    }

    /**
     * Check if device is in airplane mode
     */
    fun isAirplaneModeOn(): Boolean {
        return try {
            android.provider.Settings.Global.getInt(
                context.contentResolver,
                android.provider.Settings.Global.AIRPLANE_MODE_ON,
                0
            ) != 0
        } catch (e: Exception) {
            Timber.e(e, "Error checking airplane mode")
            false
        }
    }

    /**
     * Get a human-readable network status message
     */
    fun getNetworkStatusMessage(): String {
        return when {
            isAirplaneModeOn() -> "Airplane mode is on"
            !isNetworkAvailable() -> "No internet connection"
            isWifiConnected() -> "Connected via WiFi"
            isMobileDataConnected() -> "Connected via mobile data"
            else -> "Connected"
        }
    }

    /**
     * Check if network is metered (e.g., mobile data with limited data plan)
     */
    fun isMeteredNetwork(): Boolean {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                connectivityManager.isActiveNetworkMetered
            } else {
                isMobileDataConnected()
            }
        } catch (e: Exception) {
            Timber.e(e, "Error checking if network is metered")
            false
        }
    }

    /**
     * Enum representing different network types
     */
    enum class NetworkType {
        WIFI,
        MOBILE,
        ETHERNET,
        OTHER,
        NONE
    }

    /**
     * Enum representing network quality levels
     */
    enum class NetworkQuality {
        EXCELLENT,
        GOOD,
        FAIR,
        POOR,
        UNKNOWN,
        NO_CONNECTION
    }
}

/**
 * Extension function to execute a block only if network is available
 */
suspend inline fun <T> NetworkUtils.withNetwork(
    crossinline block: suspend () -> T
): Result<T> {
    return if (isNetworkAvailable()) {
        try {
            Result.success(block())
        } catch (e: Exception) {
            Result.error("Operation failed: ${e.message}", e)
        }
    } else {
        Result.error(
            "No internet connection. Please check your network settings.",
            code = Result.ErrorCode.NETWORK_ERROR
        )
    }
}

/**
 * Extension function to execute a block only if network quality is good
 */
suspend inline fun <T> NetworkUtils.withGoodNetwork(
    crossinline block: suspend () -> T
): Result<T> {
    return when (getNetworkQuality()) {
        NetworkUtils.NetworkQuality.EXCELLENT,
        NetworkUtils.NetworkQuality.GOOD,
        NetworkUtils.NetworkQuality.FAIR -> {
            try {
                Result.success(block())
            } catch (e: Exception) {
                Result.error("Operation failed: ${e.message}", e)
            }
        }
        NetworkUtils.NetworkQuality.POOR -> {
            Result.error(
                "Network connection is too slow. Please try again with a better connection.",
                code = Result.ErrorCode.NETWORK_ERROR
            )
        }
        else -> {
            Result.error(
                "No internet connection. Please check your network settings.",
                code = Result.ErrorCode.NETWORK_ERROR
            )
        }
    }
}