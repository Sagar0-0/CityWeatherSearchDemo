package com.sagar.moviesearchdemo.data.detail

import com.sagar.moviesearchdemo.data.WeatherApiService
import com.sagar.moviesearchdemo.data.getApiResponse
import retrofit2.Response

class WeatherDetailRepositoryImpl(
    private val api: WeatherApiService
): WeatherDetailRepository {
    override suspend fun getWeatherDetails(cityName: String): Response<WeatherDetailResponse> {
        return getApiResponse {
            api.getWeatherDetails(city = cityName)
        }
    }
}