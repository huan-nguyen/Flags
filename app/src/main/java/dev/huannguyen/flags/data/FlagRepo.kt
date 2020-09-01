package dev.huannguyen.flags.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

interface FlagRepo {
    fun flags(): Flow<DataResponse<List<FlagDataModel>>>
}

class FlagRepoImpl(
    private val webServices: WebServices
) : FlagRepo {
    override fun flags(): Flow<DataResponse<List<FlagDataModel>>> = flow {
        webServices.getFlags().body()?.let { data ->
            emit(DataResponse.Success(data))
        } ?: emit(DataResponse.Failure("Unable to retrieve data"))
    }
        .catch { emit(DataResponse.Failure("Unable to retrieve data")) }
        .flowOn(Dispatchers.IO)
}
