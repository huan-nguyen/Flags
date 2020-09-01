package dev.huannguyen.flags.di

import dev.huannguyen.flags.data.FlagRepo

object ServiceLocator {
    private lateinit var data: DataDependencies

    val flagRepo: FlagRepo by lazy { data.flagRepo }

    fun setup(dataDependencies: DataDependencies) {
        data = dataDependencies
    }
}
