package dev.huannguyen.flags.di

import dev.huannguyen.flags.App
import dev.huannguyen.flags.data.DataSyncManagerImpl
import dev.huannguyen.flags.data.FlagRepo
import dev.huannguyen.flags.connectivity.ConnectivityListener
import dev.huannguyen.flags.connectivity.ConnectivityListenerImpl
import dev.huannguyen.flags.domain.DataSyncManager

object ServiceLocator {
    private lateinit var data: DataDependencies

    val flagRepo: FlagRepo by lazy { data.flagRepo }
    val dataSyncManager: DataSyncManager by lazy { DataSyncManagerImpl(flagRepo) }

    lateinit var connectivityListener: ConnectivityListener
        private set

    fun setup(app: App) {
        data = DataDependencies(app)
        connectivityListener = ConnectivityListenerImpl(app)
    }
}
