package com.example.weatherapp.network.dataclass

import com.example.weatherapp.ui.main.layouts.dayForecastTestDatasource
import com.squareup.moshi.Json

data class WeatherForecastDay(
    val datetime: String = "",
    val datetimeEpoch: Int = 0,
    @Json(name = "tempmax") val tempMax: Double = 0.0,
    @Json(name = "tempmin") val tempMin: Double = 0.0,
    @Json(name = "temp") val avgTemp: Double = 0.0,
    @Json(name = "feelslike") val feelsLike: Double = 0.0,
    @Json(name = "humidity") val avgHumidity: Double = 0.0,
    @Json(name = "windspeed") val avgWindSpeed: Double = 0.0,
    @Json(name = "winddir") val avgWindDirection: Double = 0.0,
    @Json(name = "uvindex") val avgUvIndex: Double = 0.0,
    val sunrise: String = "",
    val sunset: String = "",
    val description: String = "",
    @Json(name = "icon") val imageIcon: String = "",
    val hours: List<WeatherHour> = dayForecastTestDatasource
)
