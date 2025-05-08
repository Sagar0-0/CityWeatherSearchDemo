package com.sagar.moviesearchdemo.data.list

import retrofit2.Response

interface CityListRepository {
    suspend fun searchForCity(query: String): Response<List<CitySearchResponseItem>>
}