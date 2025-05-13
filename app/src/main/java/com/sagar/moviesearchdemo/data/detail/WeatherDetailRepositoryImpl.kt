package com.sagar.moviesearchdemo.data.detail

import com.sagar.moviesearchdemo.data.WeatherApiService
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import java.io.IOException

class WeatherDetailRepositoryImpl(
    private val api: WeatherApiService
): WeatherDetailRepository {
    override suspend fun getWeatherDetails(cityName: String): Response<WeatherDetailResponse> {
        return try {
            api.getWeatherDetails(city = cityName)
        } catch (e: IOException) {
            Response.error(
                500,
                "Network error: ${e.localizedMessage}".toResponseBody(null)
            )
        }
    }
}