package com.example.weatherapp.ui.main

import android.util.Log
import androidx.compose.runtime.*
import com.example.weatherapp.network.WeatherApi
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.network.AutocompleteApi
import com.example.weatherapp.network.dataclass.*

enum class WeatherApiStatus { LOADING, ERROR, DONE }

class WeatherViewModel(private val weatherRepository: WeatherRepository) : ViewModel() {
    var weatherApiStatus by mutableStateOf(WeatherApiStatus.LOADING)
    var weather by mutableStateOf(Weather())
    var autocomplete by mutableStateOf(Predictions())
    var autocompleteText by mutableStateOf("")
    var selectedAddress by mutableStateOf("")
    private val _addressList = mutableStateListOf<String>()
    var isRefreshing by mutableStateOf(false)
    private val weatherList = mutableStateOf(emptyList<Weather>())
    private var databaseEmpty = true

    val addressList: List<String> = _addressList

    //https://mahendranv.github.io/posts/viewmodel-store/
    class Factory(private val repo: WeatherRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return WeatherViewModel(repo) as T
        }
    }

    init {
        Log.i("vm", "Creating vm")
        loadFromDatabase()
        getAutocomplete()

    }

    fun addAddress(addressToAdd: String){
        if (!_addressList.contains(addressToAdd)) {
            _addressList.add(addressToAdd)
        }
    }

    private fun loadFromDatabase(){
        viewModelScope.launch {
            weatherRepository.getWeatherData()
            weatherList.value = weatherRepository.getWeatherData()
            databaseEmpty = weatherList.value.isEmpty()
            if(databaseEmpty){
                setSelected("Bremen")
            }
            else {
                var findSelectedAddress = ""
                _addressList.clear()
                for(item in weatherList.value){
                    addAddress(item.address)
                    if (item.address == selectedAddress){
                        findSelectedAddress = selectedAddress
                    }
                }
                if(findSelectedAddress == ""){
                    if(weatherList.value.isNotEmpty()) {
                        setSelected(weatherList.value[0].address)
                        getWeatherData(weatherList.value[0].address)
                    }
                }
            }
        }
    }

    fun removeAddress(addressToRemove: String){
        weatherRepository.deleteWeather(weatherList.value.first { it.address == addressToRemove })
        loadFromDatabase()
    }

    fun setSelected(addressToSelect: String){
        selectedAddress = addressToSelect
        getWeatherData(addressToSelect)
    }

    fun updateAutocompleteList(value: String){
        autocompleteText = value
        getAutocomplete()
    }

    fun refresh(){
        isRefreshing = true
        getWeatherData(selectedAddress, true)
        isRefreshing = false
    }

    fun searchAddress(address: Prediction){
        resetAutofill()
        selectedAddress = address.structure.main
        getWeatherData(selectedAddress)
    }

    fun resetAutofill(){
        autocompleteText = ""
    }


    private fun getWeatherData(address: String, isRefreshing: Boolean = false) {
        viewModelScope.launch {
            weatherApiStatus = WeatherApiStatus.LOADING
            try {
                Log.i("vm", "Loading data")
                for(item in weatherList.value){
                    if (item.address == address){
                        weather = item
                        break;
                    }
                }
                if (weather.address != address || isRefreshing){
                    Log.i("vm", "Data not found in database/refreshing, loading from api.")
                    weather = WeatherApi.retrofitService.getWeather(address)
                    weatherRepository.insertWeather(weather)
                    loadFromDatabase()
                }
                weatherApiStatus = WeatherApiStatus.DONE
                Log.i("vm", "Data loaded $address")
            } catch (e: Exception) {
                weatherApiStatus = WeatherApiStatus.ERROR
                Log.i("vm", e.message.toString())
            }
        }
    }

    private fun getAutocomplete(){
        if(autocompleteText != "") {
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