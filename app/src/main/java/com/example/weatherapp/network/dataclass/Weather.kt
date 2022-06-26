package com.example.weatherapp.network.dataclass

import androidx.room.*
import com.squareup.moshi.Json

@Entity(tableName = "Weather")
data class Weather(
    @ColumnInfo(name = "latitude")
    val latitude: Double = 0.0,

    @ColumnInfo(name = "longitude")
    val longitude: Double = 0.0,

    @PrimaryKey()
    val resolvedAddress: String = "",

    @ColumnInfo(name = "address")
    val address: String = "",

    @ColumnInfo(name = "description")
    @Json(name = "description") val forecastDescription: String = "",

    @ColumnInfo(name = "forecastDays")
    val days: List<WeatherForecastDay> = List(1){ WeatherForecastDay() },

    @ColumnInfo(name = "alerts")
    val alerts: List<WeatherAlert> = List(1){ WeatherAlert() },

    @Json(name = "currentConditions")
    @ColumnInfo(name = "currentDay")
    val current: WeatherCurrentDay = WeatherCurrentDay()
    )