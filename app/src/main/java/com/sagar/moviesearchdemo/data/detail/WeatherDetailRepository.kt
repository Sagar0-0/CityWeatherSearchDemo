package com.sagar.moviesearchdemo.data.detail

import retrofit2.Response

interface WeatherDetailRepository {
    suspend fun getWeatherDetails(cityName: String): Response<WeatherDetailResponse>
}