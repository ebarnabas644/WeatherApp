package com.example.weatherapp.network.dataclass

import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
//https://www.raywenderlich.com/24509368-repository-pattern-with-jetpack-compose
class WeatherRepository(private val weatherDao: WeatherDao) {

    var result: Weather = Weather()
    suspend fun getWeatherData(): List<Weather> = weatherDao.getItems()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertWeather(item: Weather) {
        coroutineScope.launch(Dispatchers.IO) {
            weatherDao.insert(item)
        }
    }

    fun deleteWeather(item: Weather) {
        coroutineScope.launch(Dispatchers.IO) {
            weatherDao.delete(item)
        }
        Log.i("repo", item.address.toString() + " removed.")
    }

    fun updateWeather(item: Weather){
        coroutineScope.launch(Dispatchers.IO){
            weatherDao.update(item)
        }
    }

    suspend fun findByAddress(address: String){
        coroutineScope.launch(Dispatchers.Main) {
            val testResult = asyncFind(address).await()
            if (testResult != null){
                result = testResult
            }
            Log.i("repo", "Found: " + result.address.toString())
        }
    }

    /*
    suspend fun getWeatherData(){
        coroutineScope.launch(Dispatchers.IO){
            weatherData = weatherDao.getItems()
        }
    }*/

    private fun asyncFind(address: String): Deferred<Weather?> =
        coroutineScope.async(Dispatchers.IO) {
            return@async weatherDao.findByAddress(address)
        }
}