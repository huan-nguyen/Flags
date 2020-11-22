package dev.huannguyen.flags.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import dev.huannguyen.flags.App
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart

interface ConnectivityListener {
    val statuses: Flow<ConnectivityStatus>
}

enum class ConnectivityStatus {
    Connected, Disconnected
}

class ConnectivityListenerImpl(app: App) : ConnectivityListener {
    private val connectivityManager = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val statusChannel = BroadcastChannel<ConnectivityStatus>(1)
    override val statuses: Flow<ConnectivityStatus> = statusChannel.asFlow()
        .onStart { if (!isConnected()) emit(ConnectivityStatus.Disconnected) }
        .distinctUntilChanged()

    init {
        connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                statusChannel.offer(ConnectivityStatus.Connected)
            }

            override fun onLost(network: Network) {
                statusChannel.offer(ConnectivityStatus.Disconnected)
            }
        })
    }

    private fun isConnected(): Boolean {
        // Retrieve current status of connectivity
        connectivityManager.allNetworks.forEach { network ->
            val networkCapability = connectivityManager.getNetworkCapabilities(network)

            networkCapability?.let {
                if (it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                    return true
                }
            }
        }

        return false
    }
}