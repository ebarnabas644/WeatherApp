package com.example.weatherapp.network.dataclass

import com.squareup.moshi.Json

data class WeatherCurrentDay(
    val datetime: String = "",
    val datetimeEpoch: Int = 0,
    val temp: Double = 0.0,
    @Json(name = "feelslike") val feelsLike: Double = 0.0,
    val humidity: Double = 0.0,
    @Json(name = "windspeed") val windSpeed: Double? = 0.0,
    @Json(name = "winddir") val winDirection: Double? = 0.0,
    @Json(name = "uvindex") val uvIndex: Double = 0.0,
    @Json(name = "icon") val imageIcon: String = "",
    val sunrise: String = "",
    val sunset: String = ""
)
