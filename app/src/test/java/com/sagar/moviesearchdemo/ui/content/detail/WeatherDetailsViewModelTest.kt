package com.sagar.moviesearchdemo.ui.content.detail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.sagar.moviesearchdemo.data.detail.WeatherDetailRepository
import com.sagar.moviesearchdemo.data.detail.WeatherDetailResponse
import com.sagar.moviesearchdemo.ui.content.UiState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Test
import retrofit2.Response
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertIs

class WeatherDetailsViewModelTest {

    private lateinit var repository: WeatherDetailRepository
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: WeatherDetailsViewModel
    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        savedStateHandle = SavedStateHandle()
        viewModel = WeatherDetailsViewModel(repository, savedStateHandle)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Initial state is Loading`() = runTest {
        viewModel.weatherDetails.test {
            assertEquals(UiState.Loading, awaitItem())
            cancel()
        }
    }

    @Test
    fun `Successful weather data fetch`() = runTest {
        val response = WeatherDetailResponse()
        coEvery { repository.getWeatherDetails(any()) } returns Response.success(response)

        savedStateHandle["cityName"] = "Delhi"

        viewModel.weatherDetails.test {
            assertEquals(UiState.Loading, awaitItem())
            assertEquals(UiState.Success(response), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { repository.getWeatherDetails("Delhi") }
    }


    @Test
    fun `Repository returns null body`() = runTest {
        coEvery { repository.getWeatherDetails("Paris") } returns Response.success(null)

        savedStateHandle["cityName"] = "Paris"

        viewModel.weatherDetails.test {
            awaitItem() // Loading
            assertEquals(UiState.Error("No data found"), awaitItem())
            cancel()
        }

        coVerify { repository.getWeatherDetails("Paris") }
    }


    @Test
    fun `Repository returns error with error body`() = runTest {
        val errorBody = "City not found".toResponseBody()
        coEvery { repository.getWeatherDetails("Tokyo") } returns Response.error(404, errorBody)

        savedStateHandle["cityName"] = "Tokyo"

        viewModel.weatherDetails.test {
            awaitItem() // Loading
            assertEquals(UiState.Error("City not found"), awaitItem())
            cancel()
        }

        coVerify { repository.getWeatherDetails("Tokyo") }
    }

    @Test
    fun `Repository returns error with no error body`() = runTest {
        coEvery { repository.getWeatherDetails("Mars") } returns Response.error(500, "".toResponseBody(null))

        savedStateHandle["cityName"] = "Mars"

        viewModel.weatherDetails.test {
            awaitItem()
            assertIs<UiState.Error>(awaitItem())
            cancel()
        }

        coVerify(exactly = 1) { repository.getWeatherDetails("Mars") }
    }

    @Test
    fun `Fetching weather for empty city name`() = runTest {
        savedStateHandle["cityName"] = ""

        viewModel.weatherDetails.test {
            assertEquals(UiState.Loading, awaitItem())
            cancel()
        }
        coVerify(exactly = 0) { repository.getWeatherDetails(any()) }
    }

    @Test
    fun `Changing city name updates weather details`() = runTest {
        val response1 = WeatherDetailResponse()
        val response2 = WeatherDetailResponse()

        coEvery { repository.getWeatherDetails("London") } returns Response.success(response1)
        coEvery { repository.getWeatherDetails("Madrid") } returns Response.success(response2)

        viewModel.weatherDetails.test {
            savedStateHandle["cityName"] = "London"
            awaitItem() // Loading
            assertEquals(UiState.Success(response1), awaitItem())

            savedStateHandle["cityName"] = "Madrid"
            awaitItem() // Loading
            assertEquals(UiState.Success(response2), awaitItem())

            cancel()
        }

        coVerify(exactly = 2) { repository.getWeatherDetails(any()) }
    }


    @Test
    fun `Handling rapid city name changes`() = runTest {
        val first = CompletableDeferred<Response<WeatherDetailResponse>>()
        val response = WeatherDetailResponse()

        coEvery { repository.getWeatherDetails("X") } coAnswers { first.await() }
        coEvery { repository.getWeatherDetails("Y") } returns Response.success(response)

        viewModel.weatherDetails.test {
            savedStateHandle["cityName"] = "X"
            advanceTimeBy(100)
            savedStateHandle["cityName"] = "Y"
            advanceTimeBy(500)

            awaitItem() // Loading
            assertEquals(UiState.Success(response), awaitItem())
            cancel()
        }

        coVerify(exactly = 2) { repository.getWeatherDetails(any()) }
    }
}