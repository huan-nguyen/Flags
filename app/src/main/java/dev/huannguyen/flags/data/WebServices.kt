package dev.huannguyen.flags.data

import retrofit2.Response
import retrofit2.http.GET

interface WebServices {
    @GET("/flags")
    suspend fun getFlags(): Response<List<FlagDataModel>>
}
