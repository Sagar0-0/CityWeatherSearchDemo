package com.sagar.moviesearchdemo.data

import com.sagar.moviesearchdemo.data.detail.WeatherDetailResponse
import com.sagar.moviesearchdemo.data.list.CitySearchResponseItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("v1/search.json")
    suspend fun searchForCity(
        @Query("key") apiKey: String = "83b10b7c3f1d47388e7115649250705",
        @Query("q") query: String
    ): Response<List<CitySearchResponseItem>>

    @GET("v1/current.json")
    suspend fun getWeatherDetails(
        @Query("key") apiKey: String = "83b10b7c3f1d47388e7115649250705",
        @Query("q") city: String
    ): Response<WeatherDetailResponse>

}
