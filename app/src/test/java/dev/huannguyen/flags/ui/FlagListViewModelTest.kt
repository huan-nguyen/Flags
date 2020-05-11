package dev.huannguyen.flags.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import dev.huannguyen.flags.CoroutineTestRule
import dev.huannguyen.flags.R
import dev.huannguyen.flags.data.DataResponse
import dev.huannguyen.flags.data.FlagApiModel
import dev.huannguyen.flags.data.FlagRepo
import dev.huannguyen.flags.domain.Flag
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FlagListViewModelTest {
    private lateinit var repo: FlagRepo
    private lateinit var observer: LiveDataTestObserver<UiState>
    private lateinit var flagListViewModel: FlagListViewModel

    @Rule
    @JvmField
    val taskExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val coroutineTestRule = CoroutineTestRule()

    @Before
    fun setup() {
        repo = mock()
        observer = LiveDataTestObserver()
        flagListViewModel = FlagListViewModel(repo)
        flagListViewModel.flags.observeForever(observer)
    }

    @After
    fun tearDown() {
        flagListViewModel.flags.removeObserver(observer)
    }

    @Test
    fun `If flagRepo returns proper data, produce InProgress and then Success UiState`() =
        coroutineTestRule.testDispatcher.runBlockingTest {
            val fakeFlagData = listOf(
                FlagApiModel(
                    "Australia",
                    "Canberra",
                    24117360,
                    "https://flag.com/au.png",
                    "English",
                    "Australian dollar",
                    "UTC+10:00"
                ),
                FlagApiModel(
                    "Finland",
                    "Helsinki",
                    5491817,
                    "https://flag.com/fi.png",
                    "Finish",
                    "Euro",
                    "UTC+02:00"
                )
            )

            whenever(repo.getFlags()).thenReturn(DataResponse.Success(fakeFlagData))

            flagListViewModel.getFlags().join()

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

    @Test
    fun `If flagRepo returns error, produce InProgress and then Failure UiState`() =
        coroutineTestRule.testDispatcher.runBlockingTest {
            whenever(repo.getFlags()).thenReturn(DataResponse.Failure(""))

            flagListViewModel.getFlags()

            observer.assertChangedValues(
                listOf(
                    UiState.InProgress,
                    UiState.Failure(message = R.string.flag_list_error_message)
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