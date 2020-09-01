package dev.huannguyen.flags

import android.app.Application
import dev.huannguyen.flags.di.DataDependencies
import dev.huannguyen.flags.di.ServiceLocator
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        ServiceLocator.setup(DataDependencies(this))
    }
}
