package com.example.weatherapp.network.dataclass

import com.squareup.moshi.Json

data class Weather(
    val latitude: Double,
    val longitude: Double,
    val resolvedAddress: String,
    val address: String,
    @Json(name = "description") val forecastDescription: String,
    val days: List<WeatherForecastDay>,
    val alerts: List<WeatherAlert>,
    @Json(name = "currentConditions") val current: WeatherCurrentDay
    )