package com.example.weatherapp.network.dataclass

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.moshi.Json
import java.lang.reflect.Type

//https://stackoverflow.com/questions/44815784/room-persistent-database-how-to-insert-list-of-items-into-db-when-it-has-no-pr
class WeatherAlertTypeConverters {
    @TypeConverter
    fun stringToWeatherAlert(json: String?): List<WeatherAlert> {
        val gson = Gson()
        val type: Type =
            object : TypeToken<List<WeatherAlert?>?>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun weatherAlertToString(list: List<WeatherAlert?>?): String {
        val gson = Gson()
        val type: Type =
            object : TypeToken<List<WeatherAlert?>?>() {}.type
        return gson.toJson(list, type)
    }
}

data class WeatherAlert(
    val event: String = "",
    val description: String = "",
    @Json(name = "onset") val startDate: String = "",
    @Json(name = "ends") val endDate: String = "",
    val language: String = ""
)
