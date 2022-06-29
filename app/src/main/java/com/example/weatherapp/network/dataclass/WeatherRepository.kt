package com.example.weatherapp.network.dataclass

import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

//https://www.raywenderlich.com/24509368-repository-pattern-with-jetpack-compose
class WeatherRepository(private val weatherDao: WeatherDao) {

    var result: Weather = Weather()
    fun weatherList(): Flow<List<Weather>> = weatherDao.getItems()
    fun addressList(): Flow<List<String>> = weatherDao.getAddresses()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    suspend fun insertWeather(item: Weather) {
        weatherDao.insert(item)
    }

    suspend fun deleteWeather(address: String) {
        weatherDao.delete(address)
        Log.i("repo", "$address removed.")
    }

    suspend fun findByAddress(name: String): Weather {
        val findWeather: Weather = weatherDao.findByAddress(name)
        if(findWeather == null){
            return Weather()
        }
        else {
            return findWeather
        }
    }


}