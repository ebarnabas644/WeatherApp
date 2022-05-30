package com.example.weatherapp.ui.main.layouts

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.ui.main.WeatherViewModel
import androidx.compose.runtime.livedata.observeAsState
import com.example.weatherapp.network.dataclass.Weather

@Composable
fun WeatherUI(
    modifier: Modifier = Modifier,
    weatherViewModel: WeatherViewModel = viewModel()
){
    val weatherState by weatherViewModel.weather.observeAsState()
    weatherState?.let {
        WeatherScreen(
            weather = it,
            modifier = modifier
        )
    }
}

@Composable
fun WeatherScreen(
    weather: Weather,
    modifier: Modifier = Modifier
){
    Column(modifier = modifier) {
        TemperatureHeader(
            address = weather.address,
            temperature = weather.current.temp
        )
        Text("Hello")
    }
}

@Composable
fun TemperatureHeader(
    address: String,
    temperature: Double,
    modifier: Modifier = Modifier
){
    Column() {
        Text(address)
        Text(temperature.toString())
        Text("Test")
    }
}