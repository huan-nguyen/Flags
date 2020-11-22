package dev.huannguyen.flags.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import dev.huannguyen.flags.CoroutineTestRule
import dev.huannguyen.flags.data.DataResponse
import dev.huannguyen.flags.data.FlagRepo
import dev.huannguyen.flags.data.source.local.LocalFlagData
import dev.huannguyen.flags.domain.Flag
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

// TODO add all necessary tests
class FlagListViewModelTest {
    private lateinit var repo: FakeFlagRepo
    private lateinit var observer: LiveDataTestObserver<UiState>
    private lateinit var flagListViewModel: FlagListViewModel

    @get:Rule
    val liveDataTestRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Before
    fun setup() {
        observer = LiveDataTestObserver()
    }

    @Test
    fun `If flagRepo returns proper data, produce InProgress and then Success UiState`() =
        runBlockingTest {
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

            repo = FakeFlagRepo(FakeFlagRepo.Data(fakeFlagData, emptyList()))
            flagListViewModel = FlagListViewModel(repo)
            flagListViewModel.flags.observeForever(observer)

            observer.assertChangedValues(
                listOf(
                    UiState.InProgress,
                    UiState.Success(
                        data = listOf(
                            Flag(
                                "Australia",
                                "Canberra",
                                24117360,
                                "https://flag.com/au.png",
                                "English",
                                "Australian dollar",
                                "UTC+10:00"
                            ),
                            Flag(
                                "Finland",
                                "Helsinki",
                                5491817,
                                "https://flag.com/fi.png",
                                "Finish",
                                "Euro",
                                "UTC+02:00"
                            )
                        )
                    )
                )
            )
        }
}

class LiveDataTestObserver<T> : Observer<T> {
    private val values = mutableListOf<T>()

    override fun onChanged(value: T) {
        values.add(value)
    }

    fun assertChangedValues(values: List<T>) {
        assertEquals(values, this.values)
    }
}

class FakeFlagRepo(private val data: Data) : FlagRepo {
    class Data(val flags: List<LocalFlagData>, val fetch: List<LocalFlagData>)

    override fun flags(): Flow<List<LocalFlagData>> = flowOf(data.flags)

    override fun fetch(): Flow<DataResponse<List<LocalFlagData>>> = flowOf(DataResponse.Success(data.fetch))

    override suspend fun sync() {}
}
