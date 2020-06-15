package dev.huannguyen.flags.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

interface FlagRepo {
    suspend fun getFlags(): DataResponse<List<FlagDataModel>>
}

class FlagRepoImpl(
    private val webServices: WebServices
) : FlagRepo {
    override suspend fun getFlags(): DataResponse<List<FlagDataModel>> = withContext(Dispatchers.IO) {
        try {
            val response = webServices.getFlags()
            response.body()?.let { data ->
                return@withContext DataResponse.Success(data)
            }
        } catch (exception: Exception) {
            // Doing this for simplicity.
            // Should have been something more useful like logging error to reporting tool like Crashlytics or New Relic
            Timber.e("Unable to retrieve data from network.")
        }

        return@withContext DataResponse.Failure("Unable to retrieve data")
    }
}
