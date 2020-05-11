package dev.huannguyen.flags.data

import android.accounts.NetworkErrorException
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class FlagRepoTest {

    private lateinit var webServices: WebServices
    private lateinit var flagRepo: FlagRepo

    @Before
    fun setup() {
        webServices = mock()
        flagRepo = FlagRepoImpl(webServices, Dispatchers.Unconfined)
    }

    @Test
    fun `If webservices return data, produce Success data response`() = runBlockingTest {
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

        whenever(webServices.getFlags()).thenReturn(Response.success(fakeFlagData))

        val dataResponse = flagRepo.getFlags()

        assertEquals(DataResponse.Success(data = fakeFlagData), dataResponse)
    }

    @Test
    fun `If webservices return error, produce Failure data response`() = runBlockingTest {
        whenever(webServices.getFlags()).thenReturn(Response.error(400, ResponseBody.create(null, "error content")))

        val dataResponse = flagRepo.getFlags()

        assertEquals(DataResponse.Failure<FlagApiModel>(message = "Unable to retrieve data"), dataResponse)
    }

    @Test
    fun `If webservices throws exception, produce Failure data response`() = runBlockingTest {
        given(webServices.getFlags()).willAnswer { throw NetworkErrorException() }

        val dataResponse = flagRepo.getFlags()

        assertEquals(DataResponse.Failure<FlagApiModel>(message = "Unable to retrieve data"), dataResponse)
    }
}