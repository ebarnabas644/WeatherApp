package com.example.weatherapp.ui.main

import android.util.Log
import androidx.compose.runtime.*
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
    private var _selectedAddress = MutableLiveData<String>("")
    private val _status = MutableLiveData<WeatherApiStatus>()
    private val _missingAddress = MutableLiveData<Boolean>()
    var addressList: MutableList<String> by mutableStateOf(mutableListOf())

    val weather: LiveData<Weather> = _weather
    val selectedAddress: LiveData<String> = _selectedAddress
    val status: LiveData<WeatherApiStatus> = _status

    init {
        Log.i("vm", "Creating vm")
        setSelectedAddress("Bremen")
        getWeatherData()
    }

    public fun addAddress(addressToAdd: String){
        addressList.add(addressToAdd)
    }

    public fun removeAddress(addressToRemove: String){
        addressList.remove(addressToRemove)
    }

    public fun setSelectedAddress(addressToSelect: String){
        _selectedAddress.value = addressToSelect
        getWeatherData()
    }

    private fun getWeatherData() {
        if(_selectedAddress.value != "") {
            viewModelScope.launch {
                viewModelScope.launch {
                    _status.value = WeatherApiStatus.LOADING
                    try {
                        Log.i("vm", "Loading data")
                        _weather.value = WeatherApi.retrofitService.getWeather(_selectedAddress.value.toString())
                        _status.value = WeatherApiStatus.DONE
                        Log.i("vm", "Data loaded")
                    } catch (e: Exception) {
                        _status.value = WeatherApiStatus.ERROR
                        Log.i("vm", e.message.toString())
                    }
                }
            }
        }
        else{

        }
    }

}