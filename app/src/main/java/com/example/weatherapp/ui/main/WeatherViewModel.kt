package com.example.weatherapp.ui.main

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.network.AutocompleteApi
import com.example.weatherapp.network.WeatherApi
import com.example.weatherapp.network.dataclass.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


enum class WeatherApiStatus { LOADING, ERROR, DONE }

class WeatherViewModel(private val weatherRepository: WeatherRepository) : ViewModel() {
    var weatherApiStatus by mutableStateOf(WeatherApiStatus.LOADING)
    var weather by mutableStateOf(Weather())
    var autocomplete by mutableStateOf(Predictions())
    private val _autocompleteText = MutableStateFlow("")

    var selectedAddress by mutableStateOf("")
    lateinit var addressList: Flow<List<String>>
    private val _isRefreshing = MutableStateFlow(false)

    val isRefreshing: StateFlow<Boolean> = _isRefreshing
    val autoCompleteText: StateFlow<String> = _autocompleteText

    //https://mahendranv.github.io/posts/viewmodel-store/
    // Creating factory class for handling view model dependencies.
    class Factory(private val repo: WeatherRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return WeatherViewModel(repo) as T
        }
    }

    init {
        Log.i("vm", "Creating vm")
        loadFromDatabase()
    }

    // Initialize database and sync contents on startup.
    private fun loadFromDatabase(){
        viewModelScope.launch {
            addressList = weatherRepository.addressList()
            // Creating default entry if database is empty.
            if(addressList.first().isEmpty()){
                setSelected("Bremen")
            }
            else{
                setSelected(addressList.first()[0])
            }
        }
    }

    // Remove [addressToRemove] from list.
    fun removeAddress(addressToRemove: String){
        viewModelScope.launch {
            weatherRepository.deleteWeather(addressToRemove)
            //If currently selected gets deleted and address list is not empty select the first address for displaying.
            if(selectedAddress == addressToRemove){
                if(addressList.first().isNotEmpty()){
                    setSelected(addressList.first()[0])
                }
            }
        }
    }

    // Set currently selected [addressToSelect] and update currently displayed data accordingly.
    fun setSelected(addressToSelect: String){
        selectedAddress = addressToSelect
        viewModelScope.launch {
            getWeatherData(addressToSelect)
        }
    }

    // Update autocomplete text to [value] and query for new options.
    fun updateAutocompleteList(value: String){
        _autocompleteText.value = value
        getAutocomplete()
    }

    // Refresh current weather data.
    fun refresh(){
        viewModelScope.launch {
            _isRefreshing.value = true
            getWeatherData(selectedAddress)
            _isRefreshing.value = false
        }
    }

    // Set selected [address] and load new weather data.
    fun searchAddress(address: Prediction){
        resetAutofill()
        selectedAddress = address.structure.main
        viewModelScope.launch {
            getWeatherData(selectedAddress)
        }
    }

    // Reset weather address autofill text value
    fun resetAutofill(){
        _autocompleteText.value = ""
        autocomplete = Predictions()
    }


    // Loading [address] weather data from database for displaying, if not found or in case of refreshing load fresh data from api and save to database.
    private suspend fun getWeatherData(address: String) {
        weatherApiStatus = WeatherApiStatus.LOADING
        try {
            Log.i("vm", "Loading data")
            weather = weatherRepository.findByAddress(address)
            if (weather.address != address || _isRefreshing.value){
                Log.i("vm", "Data not found in database/refreshing, loading from api.")
                weather = WeatherApi.retrofitService.getWeather(address)
                weatherRepository.insertUpdateWeather(weather)
            }
            weatherApiStatus = WeatherApiStatus.DONE
            Log.i("vm", "Data loaded $address")
        } catch (e: Exception) {
            weatherApiStatus = WeatherApiStatus.ERROR
            Log.i("vm", e.message.toString())
        }
    }

    // Refresh autocomplete list based on currently entered text.
    private fun getAutocomplete(){
        if(_autocompleteText.value != "") {
            viewModelScope.launch {
                try {
                    Log.i("vm", "Loading data")
                    autocomplete =
                        AutocompleteApi.retrofitService.getAutocomplete(_autocompleteText.value)
                    Log.i("vm", "Autocomplete loaded")
                } catch (e: Exception) {
                    Log.i("vm", e.message.toString())
                }
            }
        }
    }

}