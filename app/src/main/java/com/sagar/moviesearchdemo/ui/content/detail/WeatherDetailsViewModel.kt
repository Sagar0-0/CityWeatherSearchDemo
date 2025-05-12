package com.sagar.moviesearchdemo.ui.content.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagar.moviesearchdemo.data.detail.WeatherDetailRepository
import com.sagar.moviesearchdemo.data.detail.WeatherDetailResponse
import com.sagar.moviesearchdemo.ui.content.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class WeatherDetailsViewModel @Inject constructor(
    private val repository: WeatherDetailRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val CITY_NAME_KEY = "cityName"
        private const val ERROR_NO_DATA = "No data found"
        private const val ERROR_UNKNOWN = "Unknown error"
    }

    private val cityName = savedStateHandle.getStateFlow(CITY_NAME_KEY, "")

    val weatherDetails: StateFlow<UiState<WeatherDetailResponse>> =
        cityName
            .flatMapLatest { cityName ->
                fetchWeatherDetails(cityName)
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                UiState.Loading
            )

    private fun fetchWeatherDetails(cityName: String) = flow {
        emit(UiState.Loading)
        val response = repository.getWeatherDetails(cityName)
        if (response.isSuccessful) {
            response.body()?.let { weatherDetail ->
                emit(UiState.Success(weatherDetail))
            } ?: emit(UiState.Error(ERROR_NO_DATA))
        } else {
            emit(UiState.Error(response.errorBody()?.string() ?: ERROR_UNKNOWN))
        }
    }
}