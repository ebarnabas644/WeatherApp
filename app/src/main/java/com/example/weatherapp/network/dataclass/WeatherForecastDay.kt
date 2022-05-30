package com.example.weatherapp.network.dataclass

import com.squareup.moshi.Json

data class WeatherForecastDay(
    val datetime: String,
    @Json(name = "tempmax") val tempMax: Double,
    @Json(name = "tempmin") val tempMin: Double,
    @Json(name = "temp") val avgTemp: Double,
    @Json(name = "feelslike") val feelsLike: Double,
    @Json(name = "humidity") val avgHumidity: Double,
    @Json(name = "windspeed") val avgWindSpeed: Double,
    @Json(name = "winddir") val avgWindDirection: Double,
    @Json(name = "uvindex") val avgUvIndex: Double,
    val sunrise: String,
    val sunset: String,
    val description: String,
    @Json(name = "icon") val imageIcon: String,
    val hours: List<WeatherHour>
)
