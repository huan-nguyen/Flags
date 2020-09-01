package dev.huannguyen.flags.data.source.remote

import dev.huannguyen.flags.data.WebServices

interface RemoteFlagDataSource {
    @Throws(FetchErrorException::class)
    suspend fun fetch(): List<RemoteFlagData>
}

class RemoteFlagDataSourceImpl(private val webServices: WebServices) : RemoteFlagDataSource {
    override suspend fun fetch(): List<RemoteFlagData> {
        return webServices.getFlags().body() ?: throw FetchErrorException
    }
}

object FetchErrorException : RuntimeException()
