package dev.huannguyen.flags

import android.app.Application
import dev.huannguyen.flags.di.NetworkComponent
import timber.log.Timber

/**
 * Open this class and baseUrl variable for UI testing
 */
open class App : Application() {
    open val baseUrl = "http://yourBaseUrl.example.com"
    val networkComponent by lazy { NetworkComponent(baseUrl) }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}