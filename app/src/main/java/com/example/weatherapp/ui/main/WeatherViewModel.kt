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
import androidx.compose.runtime.getValue
import com.example.weatherapp.network.AutocompleteApi
import com.example.weatherapp.network.dataclass.Prediction
import com.example.weatherapp.network.dataclass.Predictions

enum class WeatherApiStatus { LOADING, ERROR, DONE }

class WeatherViewModel : ViewModel() {
    var weatherApiStatus by mutableStateOf(WeatherApiStatus.LOADING)
    var weather by mutableStateOf(Weather())
    var autocomplete by mutableStateOf(Predictions())
    var autocompleteText by mutableStateOf("")
    var selectedAddress by mutableStateOf("")
    private val _missingAddress = MutableLiveData<Boolean>()
    private val _addressList = mutableStateListOf<String>()
    var isRefreshing by mutableStateOf(false)

    val addressList: List<String> = _addressList

    init {
        Log.i("vm", "Creating vm")
        addAddress("Bremen")
        addAddress("Budapest")
        setSelected("Bremen")
        getWeatherData()
        getAutocomplete()

    }

    fun addAddress(addressToAdd: String){
        if (!_addressList.contains(addressToAdd)) {
            _addressList.add(addressToAdd)
        }
    }

    fun removeAddress(addressToRemove: String){
        _addressList.remove(addressToRemove)
    }

    fun setSelected(addressToSelect: String){
        selectedAddress = addressToSelect
        getWeatherData()
    }

    fun updateAutocompleteList(value: String){
        autocompleteText = value
        getAutocomplete()
    }

    fun refresh(){
        isRefreshing = true
        getWeatherData()
        isRefreshing = false
    }

    fun searchAddress(address: Prediction){
        resetAutofill()
        selectedAddress = address.structure.main
        getWeatherData()
        addAddress(selectedAddress)
    }

    fun resetAutofill(){
        autocompleteText = ""
    }


    private fun getWeatherData() {
        if(selectedAddress != "") {
            viewModelScope.launch {
                viewModelScope.launch {
                    weatherApiStatus = WeatherApiStatus.LOADING
                    try {
                        Log.i("vm", "Loading data")
                        weather = WeatherApi.retrofitService.getWeather(selectedAddress.toString())
                        weatherApiStatus = WeatherApiStatus.DONE
                        Log.i("vm", "Data loaded ${selectedAddress.toString()}")
                    } catch (e: Exception) {
                        weatherApiStatus= WeatherApiStatus.ERROR
                        Log.i("vm", e.message.toString())
                    }
                }
            }
        }
        else{

        }
    }

    private fun getAutocomplete(){
        if(autocompleteText != "") {
            viewModelScope.launch {
                viewModelScope.launch {
                    try {
                        Log.i("vm", "Loading data")
                        autocomplete =
                            AutocompleteApi.retrofitService.getAutocomplete(autocompleteText)
                        Log.i("vm", "Autocomplete loaded")
                    } catch (e: Exception) {
                        Log.i("vm", e.message.toString())
                    }
                }
            }
        }
    }

}