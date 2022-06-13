package com.example.weatherapp.network.dataclass

import com.squareup.moshi.Json

data class Weather(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val resolvedAddress: String = "",
    val address: String = "",
    @Json(name = "description") val forecastDescription: String = "",
    val days: List<WeatherForecastDay> = List(1){ WeatherForecastDay() },
    val alerts: List<WeatherAlert> = List(1){ WeatherAlert() },
    @Json(name = "currentConditions") val current: WeatherCurrentDay = WeatherCurrentDay()
    )