package com.example.weatherapp.network.dataclass

import android.util.Log
import kotlinx.coroutines.flow.Flow

//https://www.raywenderlich.com/24509368-repository-pattern-with-jetpack-compose
class WeatherRepository(private val weatherDao: WeatherDao) {

    fun weatherList(): Flow<List<Weather>> = weatherDao.getItems()
    fun addressList(): Flow<List<String>> = weatherDao.getAddresses()

    // Insert new weather data record in the database, on conflict update already existing record.
    suspend fun insertUpdateWeather(item: Weather) {
        weatherDao.insert(item)
    }

    // Delete [address] weather data from database.
    suspend fun deleteWeather(address: String) {
        weatherDao.delete(address)
        Log.i("repo", "$address removed.")
    }

    // Find [address] weather data in database.
    suspend fun findByAddress(address: String): Weather {
        val findWeather: Weather = weatherDao.findByAddress(address)
        // If [address] not found in the database return an empty value instead of null.
        if(findWeather == null){
            return Weather()
        }
        else {
            return findWeather
        }
    }


}