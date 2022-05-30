package com.example.weatherapp.ui.main

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.network.WeatherApi
import com.example.weatherapp.network.dataclass.Weather
import kotlinx.coroutines.launch

enum class WeatherApiStatus { LOADING, ERROR, DONE }

class WeatherViewModel : ViewModel() {
    private val _weather = MutableLiveData<Weather>()
    val weather: LiveData<Weather> = _weather

    private val _status = MutableLiveData<WeatherApiStatus>()
    val status: LiveData<WeatherApiStatus> = _status

    init {
        Log.i("vm", "Creating vm")
        getWeatherData()
    }

    private fun getWeatherData() {
        viewModelScope.launch {
            viewModelScope.launch {
                _status.value = WeatherApiStatus.LOADING
                try {
                    Log.i("vm", "Loading data")
                    _weather.value = WeatherApi.retrofitService.getWeather("Bremen")
                    _status.value = WeatherApiStatus.DONE
                    Log.i("vm", "Data loaded")
                } catch (e: Exception) {
                    _status.value = WeatherApiStatus.ERROR
                    Log.i("vm", e.message.toString())
                }
            }
        }
    }

}