package dev.huannguyen.flags.data

import dev.huannguyen.flags.domain.DataSyncManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class DataSyncManagerImpl(private val flagRepo: FlagRepo) : DataSyncManager {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    override fun syncFlagData() {
        scope.launch {
            flagRepo.sync()
        }
    }
}
