package com.sagar.moviesearchdemo.ui.content

import androidx.compose.runtime.Stable

@Stable
sealed interface UiState<out T> {
    data object Idle : UiState<Nothing>
    data object Loading : UiState<Nothing>
    data class Error(val message: String) : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
}