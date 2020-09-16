package dev.huannguyen.flags.di

import androidx.room.Room
import dev.huannguyen.flags.App
import dev.huannguyen.flags.BuildConfig
import dev.huannguyen.flags.data.FlagRepo
import dev.huannguyen.flags.data.FlagRepoImpl
import dev.huannguyen.flags.data.WebServices
import dev.huannguyen.flags.data.source.local.Database
import dev.huannguyen.flags.data.source.local.LocalFlagDataSource
import dev.huannguyen.flags.data.source.local.LocalFlagDataSourceImpl
import dev.huannguyen.flags.data.source.remote.RemoteFlagDataSource
import dev.huannguyen.flags.data.source.remote.RemoteFlagDataSourceImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class DataDependencies(app: App) {
    private val okHttpClient = OkHttpClient.Builder()
        .apply { if (BuildConfig.DEBUG) addInterceptor(HttpLoggingInterceptor()) }
        .build()

    private val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private val webServices: WebServices = retrofit.create(WebServices::class.java)
    private val database = Room.databaseBuilder(app, Database::class.java, "app-db").build()
    private val localFlagDataSource: LocalFlagDataSource = LocalFlagDataSourceImpl(database)
    private val remoteFlagDataSource: RemoteFlagDataSource = RemoteFlagDataSourceImpl(webServices)

    val flagRepo: FlagRepo = FlagRepoImpl(localFlagDataSource, remoteFlagDataSource)
}

private const val BASE_URL = "https://flags-mobile.herokuapp.com/"

