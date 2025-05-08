package com.sagar.moviesearchdemo.ui.content

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sagar.moviesearchdemo.ui.content.detail.WeatherDetailsScreen
import com.sagar.moviesearchdemo.ui.content.detail.WeatherDetailsViewModel
import com.sagar.moviesearchdemo.ui.content.list.CityListScreen

@Composable
fun AppScreen() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "city_list"
    ) {
        composable(
            "city_list",
            enterTransition = {
                slideInHorizontally(initialOffsetX = { -it/2 })
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -it/2 })
            }
        ) {
            CityListScreen { selectedCity ->
                navController.navigate("weather_detail/${selectedCity}")
            }
        }

        composable(
            route = "weather_detail/{cityName}",
            arguments = listOf(navArgument("cityName") { type = NavType.StringType }),
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it })
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { it })
            }
        ) {
            val viewModel: WeatherDetailsViewModel = hiltViewModel()
            WeatherDetailsScreen(viewModel)
        }
    }
}