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
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class WeatherDetailsViewModel @Inject constructor(
    private val repository: WeatherDetailRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val cityName = savedStateHandle.getStateFlow(CITY_NAME_KEY, "")

    val weatherDetails: StateFlow<UiState<WeatherDetailResponse>> =
        cityName
            .flatMapLatest {
                flow {
                    val response = repository.getWeatherDetails(it)
                    if (response.isSuccessful) {
                        response.body()?.let { weatherDetail ->
                            emit(UiState.Success(weatherDetail))
                        } ?: emit(UiState.Error("No data found"))
                    } else {
                        emit(UiState.Error(response.errorBody()?.string() ?: "Unknown error"))
                    }
                }.onStart { emit(UiState.Loading) }
            }.stateIn(
                viewModelScope, SharingStarted.WhileSubscribed(5000), UiState.Loading
            )

    companion object {
        private const val CITY_NAME_KEY = "cityName"
    }
}