package dev.huannguyen.flags.data

import dev.huannguyen.flags.data.source.local.LocalFlagData
import dev.huannguyen.flags.data.source.local.LocalFlagDataSource
import dev.huannguyen.flags.data.source.remote.RemoteFlagDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

interface FlagRepo {
    fun flags(): Flow<List<LocalFlagData>>
    fun fetch(): Flow<DataResponse<List<LocalFlagData>>>
    suspend fun sync()
}

class FlagRepoImpl(
    private val localDataSource: LocalFlagDataSource,
    private val remoteDataSource: RemoteFlagDataSource
) : FlagRepo {

    override fun flags(): Flow<List<LocalFlagData>> =
        localDataSource.getFlags().flowOn(Dispatchers.IO)

    override fun fetch(): Flow<DataResponse<List<LocalFlagData>>> = flow {
        emit(DataResponse.Fetching)
        emit(fetchAndCacheData())
    }

    override suspend fun sync() {
        fetchAndCacheData()
    }

    private suspend fun fetchAndCacheData(): DataResponse<List<LocalFlagData>> =
        withContext(Dispatchers.IO) {
            try {
                val flags = remoteDataSource.fetch().map { it.toLocalData() }
                localDataSource.saveFlags(flags)
                DataResponse.Success(flags)
            } catch (exception: Exception) {
                DataResponse.Failure("Error occurs while syncing flags data")
            }
        }
}
