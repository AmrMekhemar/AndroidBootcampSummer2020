package com.example.nytarticles.networking

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.nytarticles.App
import com.example.nytarticles.R
import com.example.nytarticles.utils.toast

/**
 * Checks the Internet connection and performs an action if it's active.
 */
class NetworkStatusChecker(private val connectivityManager: ConnectivityManager?) {


    inline fun performIfConnectedToInternet(action: () -> Unit) {
        if (hasInternetConnection()) {
            action()

        } else {
            App.getAppContext()
                .toast(App.getAppContext().getString(R.string.check_internet_connection))
        }
    }

    fun hasInternetConnection(): Boolean {
        val network = connectivityManager?.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
    }
}