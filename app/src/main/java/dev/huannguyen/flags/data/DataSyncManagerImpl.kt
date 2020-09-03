package dev.huannguyen.flags.data

import dev.huannguyen.flags.connectivity.ConnectivityListener
import dev.huannguyen.flags.connectivity.ConnectivityStatus
import dev.huannguyen.flags.domain.DataSyncManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class DataSyncManagerImpl(
    private val flagRepo: FlagRepo,
    connectivityListener: ConnectivityListener
) : DataSyncManager {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    init {
        connectivityListener.statuses
            .onEach { status ->
                if (status == ConnectivityStatus.Connected) {
                    flagRepo.sync()
                }
            }
            .launchIn(scope)
    }

    override fun syncFlagData() {
        scope.launch {
            flagRepo.sync()
        }
    }
}
