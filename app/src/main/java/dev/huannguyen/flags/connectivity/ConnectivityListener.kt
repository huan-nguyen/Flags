package dev.huannguyen.flags.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import dev.huannguyen.flags.App
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

interface ConnectivityListener {
    val statuses: Flow<ConnectivityStatus>
}

enum class ConnectivityStatus {
    Connected, Disconnected
}

class ConnectivityListenerImpl(app: App) : ConnectivityListener {
    private val statusChannel = ConflatedBroadcastChannel<ConnectivityStatus>()
    override val statuses: Flow<ConnectivityStatus> = statusChannel.asFlow()

    init {
        val connectivityManager = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                // Avoid emitting status when app starts
                if (statusChannel.valueOrNull == ConnectivityStatus.Disconnected) {
                    statusChannel.offer(ConnectivityStatus.Connected)
                }
            }

            override fun onLost(network: Network) {
                statusChannel.offer(ConnectivityStatus.Disconnected)
            }
        })

        // Emit the initial status if it is Disconnected. Having to do this since if the
        // initial status is Disconnected, it is not picked up by the NetworkCallback.
        var initStatus = ConnectivityStatus.Disconnected

        // Retrieve current status of connectivity
        connectivityManager.allNetworks.forEach { network ->
            val networkCapability = connectivityManager.getNetworkCapabilities(network)

            networkCapability?.let {
                if (it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                    initStatus = ConnectivityStatus.Connected
                    return@forEach
                }
            }
        }

        if (initStatus == ConnectivityStatus.Disconnected) {
            statusChannel.offer(initStatus)
        }
    }
}