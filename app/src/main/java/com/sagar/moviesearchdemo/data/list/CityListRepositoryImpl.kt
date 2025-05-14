package com.sagar.moviesearchdemo.data.list

import com.sagar.moviesearchdemo.data.WeatherApiService
import com.sagar.moviesearchdemo.data.getApiResponse
import retrofit2.Response

class CityListRepositoryImpl(
    private val api: WeatherApiService
): CityListRepository {
    override suspend fun searchForCity(query: String): Response<List<CitySearchResponseItem>> {
        return getApiResponse {
            api.searchForCity(query = query)
        }
    }
}