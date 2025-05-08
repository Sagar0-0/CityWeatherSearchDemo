package com.sagar.moviesearchdemo.data.list

import com.sagar.moviesearchdemo.data.WeatherApiService
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.IOException

class CityListRepositoryImpl(
    private val api: WeatherApiService
): CityListRepository {
    override suspend fun searchForCity(query: String): Response<List<CitySearchResponseItem>> {
        return try{
            api.searchForCity(query = query)
        } catch (e: IOException) {
            Response.error(
                500,
                ResponseBody.create(null, "Network error: ${e.localizedMessage}")
            )
        }
    }
}