package dev.huannguyen.flags.di

import dev.huannguyen.data.di.MockWebServices
import dev.huannguyen.flags.BuildConfig
import dev.huannguyen.flags.data.WebServices
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.mock.MockRetrofit
import retrofit2.mock.NetworkBehavior
import java.util.concurrent.TimeUnit

class NetworkComponent(baseUrl: String) {
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private val mockRetrofit = MockRetrofit.Builder(retrofit)
        .networkBehavior(NetworkBehavior.create().apply {
            setDelay(2000, TimeUnit.MILLISECONDS)
        })
        .build()

    // Use MockWebServices for DEBUG build to simulate real network response.
    val webServices: WebServices =
        if (BuildConfig.DEBUG) MockWebServices(mockRetrofit)
        else retrofit.create(WebServices::class.java)
}
