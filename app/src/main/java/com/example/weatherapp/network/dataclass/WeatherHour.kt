package com.example.weatherapp.network.dataclass

import com.squareup.moshi.Json

data class WeatherHour(
    val datetime: String,
    val temp: Double,
    @Json(name = "feelslike") val feelsLike: Double,
    val humidity: Double,
    @Json(name = "windspeed") val windSpeed: Double?,
    @Json(name = "winddir") val windDirection: Double?,
    @Json(name = "uvindex") val uvIndex: Double?,
    @Json(name = "icon") val imageIcon: String?
)
