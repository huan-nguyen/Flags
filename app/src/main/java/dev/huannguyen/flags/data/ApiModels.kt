package dev.huannguyen.flags.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

sealed class DataResponse<out T> {
    data class Success<T>(val data: T) : DataResponse<T>()
    data class Failure(val message: String) : DataResponse<Nothing>()
}

@JsonClass(generateAdapter = true)
data class FlagDataModel(
    @Json(name = "country_name") val country: String,
    @Json(name = "country_capital") val capital: String,
    @Json(name = "recent_population") val population: Int,
    @Json(name = "flag_image") val url: String,
    @Json(name = "primary_language") val language: String,
    @Json(name = "primary_currency") val currency: String,
    @Json(name = "country_timezone") val timeZone: String
)
