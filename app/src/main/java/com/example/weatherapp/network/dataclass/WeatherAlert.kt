package com.example.weatherapp.network.dataclass

import com.squareup.moshi.Json

data class WeatherAlert(
    val event: String,
    val description: String,
    @Json(name = "onset") val startDate: String,
    @Json(name = "ends") val endDate: String,
    val language: String
)
