package com.sagar.moviesearchdemo.data.list

data class CitySearchResponseItem(
    val name: String = "",
    val id: Int = 0,
    val region: String = "",
    val country: String = "",
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val url: String = ""
)
