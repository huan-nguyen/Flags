package dev.huannguyen.flags.data

import dev.huannguyen.flags.data.source.local.LocalFlagData
import dev.huannguyen.flags.data.source.local.LocalFlagDataSource
import dev.huannguyen.flags.data.source.remote.RemoteFlagDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart

interface FlagRepo {
    fun flags(): Flow<List<LocalFlagData>>
    fun fetch(): Flow<DataResponse<List<LocalFlagData>>>
    fun sync()
}

class FlagRepoImpl(
    private val localDataSource: LocalFlagDataSource,
    private val remoteDataSource: RemoteFlagDataSource
) : FlagRepo {

    override fun flags(): Flow<List<LocalFlagData>> =
        localDataSource.getFlags().flowOn(Dispatchers.IO)

    override fun fetch(): Flow<DataResponse<List<LocalFlagData>>> = flow {
        try {
            delay(9000)
            val flags = remoteDataSource.fetch().map { it.toLocalData() }
            localDataSource.saveFlags(flags)
            emit(DataResponse.Success(flags))
        } catch (exception: Exception) {
            emit(DataResponse.Failure("Unable to fetch flags"))
        }
    }
        .onStart { emit(DataResponse.Fetching) }
        .flowOn(Dispatchers.IO)

    override fun sync() {

    }
}
