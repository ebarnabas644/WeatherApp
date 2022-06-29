package com.example.weatherapp.network.dataclass

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.weatherapp.ui.main.layouts.dayForecastTestDatasource
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.moshi.Json
import java.lang.reflect.Type

//https://stackoverflow.com/questions/44815784/room-persistent-database-how-to-insert-list-of-items-into-db-when-it-has-no-pr
class WeatherForecastDayTypeConverters {
    @TypeConverter
    fun stringToWeatherForecastDay(json: String?): List<WeatherForecastDay> {
        val gson = Gson()
        val type: Type =
            object : TypeToken<List<WeatherForecastDay?>?>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun weatherForecastDayToString(list: List<WeatherForecastDay?>?): String {
        val gson = Gson()
        val type: Type =
            object : TypeToken<List<WeatherForecastDay?>?>() {}.type
        return gson.toJson(list, type)
    }
}

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
