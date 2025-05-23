package com.sagar.moviesearchdemo.ui.content

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class AppRoutes {
    abstract val baseRoute: String

    open val arguments: List<NamedNavArgument>
        get() = emptyList()

    val route: String
        get() = baseRoute + if (arguments.isNotEmpty()) {
            "/${arguments.joinToString("/") { "{${it.name}}" }}"
        } else {
            ""
        }

    fun withArgument(vararg args: String): String {
        return baseRoute + if (args.isNotEmpty()) {
            "/${args.joinToString("/")}"
        } else {
            ""
        }
    }

    object CityList : AppRoutes() {
        override val baseRoute = "city_list"
    }

    object WeatherDetail : AppRoutes() {
        override val baseRoute = "weather_detail"
        override val arguments = listOf(navArgument("cityName") { type = NavType.StringType })
    }
}