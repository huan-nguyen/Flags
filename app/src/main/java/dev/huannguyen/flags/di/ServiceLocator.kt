package dev.huannguyen.flags.di

import dev.huannguyen.flags.data.DataSyncManagerImpl
import dev.huannguyen.flags.data.FlagRepo
import dev.huannguyen.flags.domain.DataSyncManager

object ServiceLocator {
    private lateinit var data: DataDependencies

    val flagRepo: FlagRepo by lazy { data.flagRepo }
    val dataSyncManager: DataSyncManager by lazy { DataSyncManagerImpl(flagRepo) }

    fun setup(dataDependencies: DataDependencies) {
        data = dataDependencies
    }
}
