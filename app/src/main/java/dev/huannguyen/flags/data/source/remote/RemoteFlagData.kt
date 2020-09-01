package dev.huannguyen.flags.data.source.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RemoteFlagData(
    @Json(name = "country_name") val country: String,
    @Json(name = "country_capital") val capital: String,
    @Json(name = "recent_population") val population: Int,
    @Json(name = "flag_image") val url: String,
    @Json(name = "primary_language") val language: String,
    @Json(name = "primary_currency") val currency: String,
    @Json(name = "country_timezone") val timeZone: String
)
