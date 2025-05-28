package com.sagar.moviesearchdemo.data

import com.sagar.moviesearchdemo.BuildConfig
import com.sagar.moviesearchdemo.data.detail.WeatherDetailResponse
import com.sagar.moviesearchdemo.data.list.CitySearchResponseItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("v1/search.json")
    suspend fun searchForCity(
        @Query("key") apiKey: String = BuildConfig.API_KEY,
        @Query("q") query: String
    ): Response<List<CitySearchResponseItem>>

    @GET("v1/current.json")
    suspend fun getWeatherDetails(
        @Query("key") apiKey: String = BuildConfig.API_KEY,
        @Query("q") city: String
    ): Response<WeatherDetailResponse>

}
