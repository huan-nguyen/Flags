package dev.huannguyen.flags.di

import dev.huannguyen.flags.data.WebServices
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object NetworkComponent {
    private const val BASE_URL = "https://flags-mobile.herokuapp.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val webServices: WebServices = retrofit.create(WebServices::class.java)
}
