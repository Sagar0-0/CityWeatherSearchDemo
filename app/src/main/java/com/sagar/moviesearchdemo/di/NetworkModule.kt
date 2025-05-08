package com.sagar.moviesearchdemo.di

import com.sagar.moviesearchdemo.data.WeatherApiService
import com.sagar.moviesearchdemo.data.detail.WeatherDetailRepository
import com.sagar.moviesearchdemo.data.detail.WeatherDetailRepositoryImpl
import com.sagar.moviesearchdemo.data.list.CityListRepository
import com.sagar.moviesearchdemo.data.list.CityListRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "http://api.weatherapi.com/v1/"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherApiService(retrofit: Retrofit): WeatherApiService {
        return retrofit.create(WeatherApiService::class.java)
    }

    @Provides
    fun provideCityListRepository(api: WeatherApiService): CityListRepository {
        return CityListRepositoryImpl(api)
    }

    @Provides
    fun provideWeatherDetailRepository(api: WeatherApiService): WeatherDetailRepository {
        return WeatherDetailRepositoryImpl(api)
    }
}
