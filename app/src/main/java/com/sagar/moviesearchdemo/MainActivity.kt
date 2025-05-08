package com.sagar.moviesearchdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.sagar.moviesearchdemo.ui.content.AppScreen
import com.sagar.moviesearchdemo.ui.theme.MovieSearchDemoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovieSearchDemoTheme {
                AppScreen()
            }
        }
    }
}