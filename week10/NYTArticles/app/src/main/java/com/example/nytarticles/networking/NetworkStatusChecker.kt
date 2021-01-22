package com.example.nytarticles.networking

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.nytarticles.App
import com.example.nytarticles.R
import com.example.nytarticles.utils.toast
import org.koin.core.KoinComponent
import org.koin.core.inject
import splitties.init.appCtx

/**
 * Checks the Internet connection and performs an action if it's active.
 */
class NetworkStatusChecker() : KoinComponent {
    private val connectivityManager: ConnectivityManager by inject()
    inline fun performIfConnectedToInternet(action: () -> Unit) {
        if (hasInternetConnection()) {
            action()

        } else {
            appCtx.toast(appCtx.getString(R.string.check_internet_connection))
        }
    }

    fun hasInternetConnection(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
    }
}