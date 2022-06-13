package com.example.weatherapp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector


enum class WeatherScreen{
    WeatherUI(),
    SearchUI(),
    ForecastUI();

    companion object {
        fun fromRoute(route: String?): WeatherScreen =
            when (route?.substringBefore("/")) {
                WeatherUI.name -> WeatherUI
                SearchUI.name -> SearchUI
                ForecastUI.name -> ForecastUI
                null -> WeatherUI
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
        fun convertScreenToResourceId(screen: WeatherScreen): Int{
            return when(screen){
                WeatherUI -> R.string.navbar_today
                SearchUI -> R.string.navbar_search
                ForecastUI -> R.string.navbar_next_2_week
            }
        }
        fun convertScreenToIcon(screen: WeatherScreen): ImageVector{
            return when(screen){
                WeatherUI -> Icons.Filled.Home
                SearchUI -> Icons.Filled.Search
                ForecastUI -> Icons.Filled.DateRange
            }
        }
    }
}