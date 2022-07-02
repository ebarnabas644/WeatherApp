package com.example.weatherapp.network.dataclass

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.moshi.Json
import java.lang.reflect.Type

//https://stackoverflow.com/questions/44815784/room-persistent-database-how-to-insert-list-of-items-into-db-when-it-has-no-pr
class WeatherCurrentDayTypeConverters {
    @TypeConverter
    fun stringToWeatherCurrentDay(json: String?): WeatherCurrentDay {
        val gson = Gson()
        val type: Type =
            object : TypeToken<WeatherCurrentDay?>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun weatherCurrentDayToString(item: WeatherCurrentDay?): String {
        val gson = Gson()
        val type: Type =
            object : TypeToken<WeatherCurrentDay?>() {}.type
        return gson.toJson(item, type)
    }
}

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
