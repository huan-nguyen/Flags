package dev.huannguyen.flags.data

import dev.huannguyen.flags.data.source.local.LocalFlagData
import dev.huannguyen.flags.data.source.local.LocalFlagDataSource
import dev.huannguyen.flags.data.source.remote.FetchErrorException
import dev.huannguyen.flags.data.source.remote.RemoteFlagData
import dev.huannguyen.flags.data.source.remote.RemoteFlagDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class FlagRepoTest {
    private lateinit var localDataSource: FakeLocalDataSource
    private lateinit var remoteDataSource: FakeRemoteDataSource
    private lateinit var flagRepo: FlagRepo

    @Test
    fun `Get local data from LocalDataSource`() = runBlocking {
        val fakeFlagData = listOf(
            LocalFlagData(
                "Australia",
                "Canberra",
                24117360,
                "https://flag.com/au.png",
                "English",
                "Australian dollar",
                "UTC+10:00"
            ),
            LocalFlagData(
                "Finland",
                "Helsinki",
                5491817,
                "https://flag.com/fi.png",
                "Finish",
                "Euro",
                "UTC+02:00"
            )
        )

        localDataSource = FakeLocalDataSource(fakeFlagData)
        remoteDataSource = FakeRemoteDataSource(FakeRemoteDataSource.Behaviour.Success(emptyList()))
        flagRepo = FlagRepoImpl(localDataSource, remoteDataSource)

        val dataResponse = flagRepo.flags()

        dataResponse.collect {
            assertEquals(fakeFlagData, it)
        }
    }

    @Test
    fun `On fetching data, return Failure response if RemoteDataSource returns an exception`() =
        runBlocking {
            localDataSource = FakeLocalDataSource(emptyList())
            remoteDataSource = FakeRemoteDataSource(FakeRemoteDataSource.Behaviour.Throw)
            flagRepo = FlagRepoImpl(localDataSource, remoteDataSource)

            val dataResponse = flagRepo.fetch().toList()

            assertEquals(DataResponse.Fetching, dataResponse[0])
            assertEquals(DataResponse.Failure(message = "Error occurs while syncing flags data"), dataResponse[1])
        }

    @Test
    fun `On fetching data, return Success response if RemoteDataSource returns data`() {
        // TODO
    }
}

class FakeLocalDataSource(private var defaultData: List<LocalFlagData>) : LocalFlagDataSource {
    override fun getFlags(): Flow<List<LocalFlagData>> = flowOf(defaultData)

    override fun saveFlags(data: List<LocalFlagData>) {
        this.defaultData = data
    }
}

class FakeRemoteDataSource(private val behaviour: Behaviour) : RemoteFlagDataSource {
    sealed class Behaviour {
        object Throw : Behaviour()
        data class Success(val data: List<RemoteFlagData>) : Behaviour()
    }

    override suspend fun fetch(): List<RemoteFlagData> = when (behaviour) {
        is Behaviour.Throw -> throw FetchErrorException
        is Behaviour.Success -> behaviour.data
    }
}
