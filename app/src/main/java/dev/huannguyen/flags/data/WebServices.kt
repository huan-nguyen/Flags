package dev.huannguyen.flags.data

import dev.huannguyen.flags.data.source.remote.RemoteFlagData
import retrofit2.Response
import retrofit2.http.GET

interface WebServices {
    @GET("/flags")
    suspend fun getFlags(): Response<List<RemoteFlagData>>
}
